/*
 *  Copyright 2014 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm
package dependency

import java.util.ArrayList;
// import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
// import java.util.Set;
import java.util.stream.IntStream;

import org.teavm.cache.IncrementalDependencyRegistration;
import org.teavm.diagnostics.Diagnostics;
import org.teavm.model.BasicBlock;
import org.teavm.model.CallLocation;
import org.teavm.model.ClassHierarchy;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderSource;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassHolderTransformerContext;
import org.teavm.model.ClassReader;
import org.teavm.model.ClassReaderSource;
import org.teavm.model.Instruction;
import org.teavm.model.InvokeDynamicInstruction;
import org.teavm.model.MethodHolder;
import org.teavm.model.MethodReference;
import org.teavm.model.Program;
import org.teavm.model.ValueType;
import org.teavm.model.emit.ProgramEmitter;
import org.teavm.model.emit.ValueEmitter;
import org.teavm.model.instructions.AssignInstruction;
import org.teavm.model.instructions.NullConstantInstruction;
import org.teavm.model.optimization.UnreachableBasicBlockEliminator;
import org.teavm.model.transformation.ClassInitInsertion;
import org.teavm.model.util.BasicBlockSplitter;
import org.teavm.model.util.ModelUtils;

object DCS2 :

    /**
     * intended to be used as a workaround for uninitialized fields in DependencyClassSource, which are initialized in the constructor body, but are needed in the initialization of other fields. This is a hack, but it allows to avoid refactoring the whole class.
     */
    def UNINITIALIZED[R]: R
    = null.asInstanceOf[Any].asInstanceOf

class DependencyClassSource(
    private var diagnostics : Diagnostics,
    agent       : DependencyAgent   ,
    innerSource : ClassReaderSource ,
    dependencyRegistration : IncrementalDependencyRegistration,
    private[dependency] var innerHierarchy: ClassHierarchy    = DCS2.UNINITIALIZED,
    private var generatedClasses : Map[String, ClassHolder] = Map(),
    private var transformers : Seq[ClassHolderTransformer] = Seq() ,
    private[dependency] var obfuscated : Boolean = false,
    private[dependency] var strict : Boolean = false,
    private[dependency] var cache : Map[String, Option[ClassHolder] ] = Map(),
    private var referenceResolver : ReferenceResolver = DCS2.UNINITIALIZED,
    private var classInitInsertion : ClassInitInsertion = DCS2.UNINITIALIZED,
    private var entryPoint : String = DCS2.UNINITIALIZED,
    val platformTags: Seq[String] = DCS2.UNINITIALIZED ,
) extends ClassHolderSource
{ thisDcs =>
    import language.unsafeNulls
    import scala.jdk.CollectionConverters.{given}

    /** convenience retained for existing Java code */
    def cacheC() = cache.asJava

    /**
     * convenience retained for existing Java code
     */
    def this(agent: DependencyAgent, innerSource: ClassReaderSource, dx: Diagnostics, dependencyRegistration: IncrementalDependencyRegistration, platformTags: Array[String]) =
        this(
            agent = agent,
            innerSource = innerSource,
            diagnostics = dx,
            dependencyRegistration = dependencyRegistration,
            platformTags = platformTags.toSeq,
            // innerHierarchy = DCS2.UNINITIALIZED ,
        )

    innerHierarchy = new ClassHierarchy(innerSource);

    referenceResolver = new ReferenceResolver(thisDcs, java.util.List.of(platformTags*), diagnostics )

    def getReferenceResolver() = referenceResolver

    classInitInsertion = new ClassInitInsertion(thisDcs)

    private var bootstrapMethodSubstitutors: Map[MethodReference, BootstrapMethodSubstitutor] = Map()
    private var bsmDefaultSubst: BootstrapMethodSubstitutor = DCS2.UNINITIALIZED

    def addBootstrapMethodSubstitutor(method: MethodReference, substitutor: BootstrapMethodSubstitutor): Unit =
        bootstrapMethodSubstitutors = bootstrapMethodSubstitutors.updated (method, substitutor)

    def setBootstrapMethodSubstitutorDefault(substitutor: BootstrapMethodSubstitutor): Unit =
        bsmDefaultSubst = substitutor

    private var disposed: Boolean = false
    private var usedMethods: Set[MethodReference] = Set()
    private var pendingErrors: Map[MethodReference, Seq[() => Unit] ] = Map()

    def get(name: String): ClassHolder =
        cache.get(name) match
            case Some(result) => result.orNull
            case None =>
                val cls = findClass(name)
                cache = cache.updated (name, Option(cls))
                if (cls != null) transformClass(cls)
                cls

    def submit(cls: ClassHolder): Unit =
        if (innerSource.get(cls.getName()) != null || generatedClasses.contains(cls.getName())) {
            throw new IllegalArgumentException(s"Class ${cls.getName} is already defined")
        }
        val clsToSubmit = if (transformers.nonEmpty) ModelUtils.copyClass(cls) else cls
        generatedClasses = generatedClasses.updated (cls.getName, clsToSubmit)
        for (method <- clsToSubmit.getMethods.asScala) {
            if (method.getProgram != null && method.getProgram.basicBlockCount() > 0) {
                new UnreachableBasicBlockEliminator().optimize(method.getProgram)
            }
        }

    protected def transformClass(cls: ClassHolder): Unit =
        if (!disposed) {
            for (method <- cls.getMethods.asScala) {
                processInvokeDynamic(method)
            }
        }
        if (transformers.nonEmpty) {
            for (transformer <- transformers) {
                transformer.transformClass(cls, transformContext)
            }
        }
        for (method <- cls.getMethods.asScala) {
            if (method.getProgram != null) {
                val program = method.getProgram
                method.setProgramSupplier(m => {
                    if (disposed) null
                    else {
                        referenceResolver.resolve(m, program)
                        classInitInsertion.apply(m, program)
                        program
                    }
                })
            }
        }

    def findClass(name: String): ClassHolder =
        innerSource.get(name) match
            case null => generatedClasses.getOrElse(name, null)
            case cls => ModelUtils.copyClass(cls)

    def getGeneratedClassNames(): java.util.Collection[? <: String] =
        java.util.List.copyOf(generatedClasses.keySet.asJava)

    def isGeneratedClass(className: String): Boolean =
        generatedClasses.contains(className)

    def addTransformer(transformer: ClassHolderTransformer): Unit =
        transformers = transformers :+ transformer

    def setEntryPoint(entryPoint: String): Unit =
        this.entryPoint = entryPoint

    /**
     * prints the listing of all classes in the cache, grouped by package, to the standard error stream.
     * For each package, it prints the number of classes in that package and the list of class names (without the package prefix) in that package.
     * This is useful for debugging purposes to see which classes are being loaded and transformed.
     * 
     */
    def dumpListing(): Unit =
        locally :
            val pkgs =
                cache
                .groupBy({ case (k, v) => k.pipe { _.split("\\.").toSeq }.dropRight(1).mkString(".") })
                .map { case (k, v) => (k, (n = v.size, items = for { v1 <- v.keySet } yield v1.pipe { _.split("\\.").toSeq }.last )) }
                .toSeq
                .sortBy { case (k, v) => k }
            for { (pkg, pkgInf) <- pkgs } do
                System.err.println { s"package ${pkg}: ${pkgInf.n}" }
                if pkg matches "java\\.(lang|util)" then
                    System.err.println :
                        pkgInf.items
                        .toSeq
                        .sorted
                        .mkString(", ")
                        .indent(2)

    def dispose(): Unit =
      locally :
        dumpListing()
      locally :
        transformers = Seq()
        bootstrapMethodSubstitutors = Map()
        disposed = true

    protected def processInvokeDynamic(method: MethodHolder): Unit
    =
        val program = method.getProgram();
        if (program == null) {
            return;
        }

        val pe = ProgramEmitter.create(program, innerHierarchy);
        val splitter = new BasicBlockSplitter(program);
        for (i <- Range(0, program.basicBlockCount() ) ) {
            val block = program.basicBlockAt(i);
            for (insn <- block.asScala ) {
              val block = insn.getBasicBlock()

              if (insn.isInstanceOf[InvokeDynamicInstruction]) then
                val indy = insn.asInstanceOf[InvokeDynamicInstruction]
                val bootstrapMethod = MethodReference(indy.getBootstrapMethod().getClassName(),
                                                      indy.getBootstrapMethod().getName(),
                                                      indy.getBootstrapMethod().signature()* )

                lazy val arguments =
                            var arguments: Seq[ValueEmitter] =
                                Seq()
                            arguments = arguments ++ (0 until indy.getArguments.size() ).map(k => pe.`var`(indy.getArguments().get(k), indy.getMethod().parameterType(k)) )
                            arguments

                lazy val callSite = DynamicCallSite(
                            method.getReference(), indy.getMethod(),
                            if (indy.getInstance() != null) then pe.`var`(indy.getInstance, ValueType.`object`(method.getOwnerName)) else null,
                            java.util.List.of(arguments*) , indy.getBootstrapMethod(), indy.getBootstrapArguments(),
                            agent, insn.getLocation)

                lazy val entered = locally[Unit] :
                      pe.enter(block)
                      pe.setCurrentLocation(indy.getLocation)

                lazy val location = CallLocation(method.getReference, insn.getLocation)

                def applyFallbackNullInsn() =
                        val nullInsn = NullConstantInstruction()
                        nullInsn.setReceiver(indy.getReceiver());
                        nullInsn.setLocation(indy.getLocation());
                        insn.replace(nullInsn)

                lazy val splitBlock = splitter.split(block, insn)

                val substitutors = (Seq() :++ bootstrapMethodSubstitutors.get(bootstrapMethod) :++ Option(bsmDefaultSubst) )
                if substitutors.isEmpty then
                        applyFallbackNullInsn()
                        reportError(location, "[DependencyClassSource::processInvokeDynamic] (NOTFOUND 011) no Substitutor for Bootstrap Method {{m0}}", bootstrapMethod)
                else
                  val substItr = substitutors.iterator
                  untilTrueDo :
                    substItr.nextOption().match
                    case None =>
                            applyFallbackNullInsn()
                            reportError(location, "[DependencyClassSource::processInvokeDynamic] (NOTFOUND 016) neither Substitute successfully did for Bootstrap Method {{m0}}; those Substitutor(s) were {{m1}}", bootstrapMethod, substitutors )
                            true

                    case Some(substitutor) =>
                      if (substitutor == null) then
                            applyFallbackNullInsn()
                            reportError(location, "[DependencyClassSource::processInvokeDynamic] (FATAL 07) found 'null' being passed in place of Substitutor instance, while handling Bootstrap Method {{m0}}", bootstrapMethod)
                            true
                      else

                        splitBlock
                        entered

                        arguments

                        callSite

                        val result = substitutor.substitute(callSite, pe)
                        if result == null then false else
                            insn.delete()

                            if (result.getVariable() != null && result.getVariable() != indy.getReceiver() && indy.getReceiver() != null) {
                                val assign = AssignInstruction()
                                assign.setAssignee(result.getVariable())
                                assign.setReceiver(indy.getReceiver())
                                pe.addInstruction(assign)
                            }

                            pe.jump(splitBlock);

                            true

            }
        }
        splitter.fixProgram();

    def reportError(location: CallLocation, message: String, args: Any*): Unit =
        if (usedMethods.contains(location.getMethod())) {
            diagnostics.error(location, message, args*)
        } else {
            val error = () => diagnostics.error(location, message, args*)
            pendingErrors.get(location.getMethod) match
                case Some(errors) => pendingErrors = pendingErrors.updated (location.getMethod, errors :+ error)
                case None => pendingErrors = pendingErrors.updated (location.getMethod, Seq(error))
        }

    def use(method: MethodReference): Unit =
        usedMethods = usedMethods + method
        val errors = pendingErrors.get(method) tap { _ => pendingErrors = pendingErrors - method }
        errors match
            case Some(errors) =>
                for (error <- errors) {
                    error()
                }
            case None => ()

    val transformContext: ClassHolderTransformerContext
    =
        new ClassHolderTransformerContext {
            def getHierarchy() = innerHierarchy
            def getDiagnostics() = diagnostics
            def getIncrementalCache() = dependencyRegistration
            def isObfuscated() = obfuscated
            def isStrict() = strict
            def submit(cls: ClassHolder) = thisDcs.submit(cls)
            def getEntryPoint() = entryPoint
        }

}











