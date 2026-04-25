package org.teavm.vm



import java.io.File;
import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collection;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.LinkedHashSet;
// import java.util.List;
// import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
// import java.util.Set;
// import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.teavm.cache.AlwaysStaleCacheStatus;
import org.teavm.cache.AnnotationAwareCacheStatus;
import org.teavm.cache.CacheStatus;
import org.teavm.cache.EmptyProgramCache;
import org.teavm.cache.ProgramDependencyExtractor;
import org.teavm.common.ServiceRepository;
import org.teavm.dependency.BootstrapMethodSubstitutor;
import org.teavm.dependency.ClassSourcePacker;
import org.teavm.dependency.DependencyAnalyzer;
import org.teavm.dependency.DependencyInfo;
import org.teavm.dependency.DependencyListener;
import org.teavm.dependency.DependencyPlugin;
import org.teavm.dependency.Linker;
import org.teavm.dependency.MethodDependencyInfo;
import org.teavm.diagnostics.AccumulationDiagnostics;
import org.teavm.diagnostics.Diagnostics;
import org.teavm.diagnostics.ProblemProvider;
import org.teavm.model.BasicBlock;
import org.teavm.model.ClassHierarchy;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderSource;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassReader;
import org.teavm.model.ClassReaderSource;
import org.teavm.model.ElementModifier;
import org.teavm.model.FieldHolder;
import org.teavm.model.FieldReference;
import org.teavm.model.Instruction;
import org.teavm.model.ListableClassHolderSource;
import org.teavm.model.ListableClassReaderSource;
import org.teavm.model.MethodDescriptor;
import org.teavm.model.MethodHolder;
import org.teavm.model.MethodReader;
import org.teavm.model.MethodReference;
import org.teavm.model.MutableClassHolderSource;
import org.teavm.model.Program;
import org.teavm.model.ProgramCache;
import org.teavm.model.ValueType;
import org.teavm.model.analysis.ClassInitializerAnalysis;
import org.teavm.model.analysis.ClassInitializerInfo;
import org.teavm.model.instructions.ExitInstruction;
import org.teavm.model.instructions.InitClassInstruction;
import org.teavm.model.instructions.InvokeInstruction;
import org.teavm.model.optimization.ArrayUnwrapMotion;
import org.teavm.model.optimization.ClassInitElimination;
import org.teavm.model.optimization.ConstantConditionElimination;
import org.teavm.model.optimization.DefaultInliningStrategy;
import org.teavm.model.optimization.Devirtualization;
import org.teavm.model.optimization.GlobalValueNumbering;
import org.teavm.model.optimization.Inlining;
import org.teavm.model.optimization.InliningStrategy;
import org.teavm.model.optimization.LoopInvariantMotion;
import org.teavm.model.optimization.MethodOptimization;
import org.teavm.model.optimization.MethodOptimizationContext;
import org.teavm.model.optimization.RedundantJumpElimination;
import org.teavm.model.optimization.RedundantNullCheckElimination;
import org.teavm.model.optimization.RedundantPhiElimination;
import org.teavm.model.optimization.RepeatedFieldReadElimination;
import org.teavm.model.optimization.ScalarReplacement;
import org.teavm.model.optimization.SystemArrayCopyOptimization;
import org.teavm.model.optimization.UnreachableBasicBlockElimination;
import org.teavm.model.optimization.UnusedVariableElimination;
import org.teavm.model.text.ListingBuilder;
import org.teavm.model.transformation.ClassInitializerInsertionTransformer;
import org.teavm.model.util.ModelUtils;
import org.teavm.model.util.ProgramUtils;
import org.teavm.model.util.RegisterAllocator;
import org.teavm.parsing.resource.ResourceProvider;
import org.teavm.vm.spi.ClassFilter;
import org.teavm.vm.spi.TeaVMHost;
import org.teavm.vm.spi.TeaVMHostExtension;
import org.teavm.vm.spi.TeaVMPlugin;



import language.unsafeNulls

import scala.jdk.CollectionConverters.{*, given}

object TeaVMLinkerDefaults {

    /**
     * the default list of optimisations for the given target and optimisation level.
     * 
     */
    def getDefaultOptimizations(target: TeaVMTarget, optimizationLevel: TeaVMOptimizationLevel)
        : Seq[MethodOptimization]
    =
        var optimizations: Seq[MethodOptimization] = Seq()
        optimizations = optimizations :+ RedundantJumpElimination()
        optimizations = optimizations :+ ArrayUnwrapMotion()
        if (optimizationLevel.ordinal() >= TeaVMOptimizationLevel.ADVANCED.ordinal()) {
            optimizations = optimizations :+ ScalarReplacement()
            //optimizations.add(new LoopInversion());
            optimizations = optimizations :+ LoopInvariantMotion()
        }
        if (optimizationLevel.ordinal() >= TeaVMOptimizationLevel.ADVANCED.ordinal()) {
            optimizations = optimizations :+ RepeatedFieldReadElimination()
        }
        optimizations = optimizations :+ GlobalValueNumbering(optimizationLevel == TeaVMOptimizationLevel.SIMPLE)
        optimizations = optimizations :+ RedundantNullCheckElimination()
        if (optimizationLevel.ordinal() >= TeaVMOptimizationLevel.ADVANCED.ordinal()) {
            optimizations = optimizations :+ ConstantConditionElimination()
            optimizations = optimizations :+ RedundantJumpElimination()
            optimizations = optimizations :+ UnusedVariableElimination()
        }
        optimizations = optimizations :+ ClassInitElimination()
        optimizations = optimizations :+ UnreachableBasicBlockElimination()
        optimizations = optimizations :+ UnusedVariableElimination()
        if (target.needsSystemArrayCopyOptimization()) {
            optimizations = optimizations :+ SystemArrayCopyOptimization()
        }
        optimizations = optimizations :+ RedundantPhiElimination()
        optimizations

    def optimizeMethodImpl(using target: TeaVMTarget, optimizationLevel: TeaVMOptimizationLevel, dependencyAnalyzer: DependencyAnalyzer)(method: MethodHolder, optimizedProgram: Program ): Program =
        target.beforeOptimizations(optimizedProgram, method)

        if optimizedProgram.basicBlockCount() > 0 then
            val context = new GeneralisedMethodOptimizationContextImpl(method, dependencyAnalyzer = dependencyAnalyzer )
            var changed: Boolean = false
            while
                changed = false
                for optimization <- getDefaultOptimizations(target, optimizationLevel) do
                    try
                        changed |= optimization.optimize(context, optimizedProgram)
                    catch case e: (Exception | AssertionError) =>
                        val listingBuilder = new ListingBuilder()
                        try
                            val listing = listingBuilder.buildListing(optimizedProgram, "")
                            System.err.println(s"Error optimizing program for method ${method.getReference()}:\n$listing")
                        catch case e2: RuntimeException =>
                            System.err.println(s"Error optimizing program for method ${method.getReference()}")
                            // do nothing
                        throw new RuntimeException(e)
                changed
            do { }

            target.afterOptimizations(optimizedProgram, method)
            val categoryProvider = target.variableCategoryProvider()
            if categoryProvider != null then
                val allocator = new RegisterAllocator(categoryProvider)
                allocator.allocateRegisters(method.getReference(), optimizedProgram,
                        optimizationLevel == TeaVMOptimizationLevel.SIMPLE)

        optimizedProgram

    private[teavm]
    class GeneralisedMethodOptimizationContextImpl(method: MethodReader, dependencyAnalyzer: DependencyAnalyzer) extends MethodOptimizationContext {

        override def getMethod(): MethodReader = method

        override def getDependencyInfo(): DependencyInfo = dependencyAnalyzer

        override def getClassSource(): ClassReaderSource = dependencyAnalyzer.getClassSource()

        override def getHierarchy(): ClassHierarchy = dependencyAnalyzer.getClassHierarchy()

    }

} // TeaVMLinkerDefaults


