/*
 *  Copyright 2016 Alexey Andreev.
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
package org.teavm.vm;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.teavm.dependency.DependencyAnalyzer;
import org.teavm.dependency.DependencyListener;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ListableClassHolderSource;
import org.teavm.model.ListableClassReaderSource;
import org.teavm.model.MethodReader;
import org.teavm.model.MethodReference;
import org.teavm.model.Program;
import org.teavm.model.optimization.InliningFilterFactory;
import org.teavm.model.util.VariableCategoryProvider;
import org.teavm.vm.spi.TeaVMHostExtension;

public interface TeaVMTarget {
    List<ClassHolderTransformer> getTransformers();

    /**
     * subclasses must override this method to return a list of dependency listeners that will be used during the compilation. The listeners will be notified about all dependencies that are added to the compilation, and can be used to add additional dependencies, or to remove dependencies that are not needed for the target platform. The target should return an empty list if it does not need to listen for dependencies.
     */
    List<DependencyListener> getDependencyListeners();

    /**
     * 
     * @param entryPoint the entry point method, which is the method that is being compiled. The target can use this information to determine the entry point of the program, and to optimize the code for the entry point. The target should not modify the entry point method, as it is used by the compiler to determine the entry point of the program.
     * @param name the name of the entry point, which is the name of the method that is being compiled. The target can use this information to determine the entry point of the program, and to optimize the code for the entry point. The target should not modify the name of the entry point, as it is used by the compiler to determine the entry point of the program.
     */
    default void setEntryPoint(String entryPoint, String name) {
    }

    /**
     * called by {@link TeaVM} to set the controller for the target. The controller can be used by the target to interact with the compiler, for example, to add dependencies or to request optimizations. The target should not keep a reference to the controller after the compilation is finished, as it may cause memory leaks.
     */
    void setController(TeaVMTargetController controller);

    List<TeaVMHostExtension> getHostExtensions();

    VariableCategoryProvider variableCategoryProvider();

    /**
     * called by {@link TeaVM} to allow the target to contribute dependencies. This method is called before any analysis is done, so the target can add dependencies that are not visible in the program, or that are not visible in the bytecode. This is used to add dependencies on classes and methods that are not referenced in the program, but are required for the target platform, or that are implemented in a different way on the target platform.
     */
    void contributeDependencies(DependencyAnalyzer dependencyAnalyzer);

    /**
     * called before inlining, so that the target can analyze the program and method, and prepare for optimizations. This method is called only once per method, and is not called for methods that are not being inlined.
     * 
     * @param program the program being compiled. The target can analyze the program to determine if it can be optimized for the target platform, and to prepare for optimizations. The target should not modify the program in this method, as it is called before any optimizations are done.
     * @param method the method being compiled. The target can analyze the method to determine if it can be optimized for the target platform, and to prepare for optimizations. The target should not modify the method in this method, as it is called before any optimizations are done.
     */
    default void beforeInlining(Program program, MethodReader method) {
    }

    default void analyzeBeforeOptimizations(ListableClassReaderSource classSource) {
    }

    /**
     * called every time before optimizations, so that the target can analyze the program and method, and prepare for optimizations. This method is called for every method, even for methods that are not being inlined.
     * 
     * @param program the program being compiled. The target can analyze the program to determine if it can be optimized for the target platform, and to prepare for optimizations. The target should not modify the program in this method, as it is called before any optimizations are done.
     * @param method the method being compiled.
     */
    void beforeOptimizations(Program program, MethodReader method);

    void afterOptimizations(Program program, MethodReader method);

    /**
     * called in the end of the compilation, so that the target can emit the result. The target should not modify the program in this method, as it is called after all optimizations and transformations are done.
     * 
     * @param classes the source of class definitions that are being compiled. The target can use this source to read class definitions that are being compiled, and to determine the structure of the program. The target should not modify the class source in this method, as it is used by the compiler to read class definitions.
     * @param buildTarget the target to which the result should be emitted. The target can use this target to emit the result of the compilation, for example, to write the output to a file or to a network socket. The target should not modify the build target in this method, as it is used by the compiler to emit the result.
     * @param outputName the name of the output file or resource. The target can use this name to determine the name of the output file or resource, and to optimize the code for the output. The target should not modify the output name in this method, as it is used by the compiler to determine the name of the output file or resource.
     * 
     */
    void emit(ListableClassHolderSource classes, BuildTarget buildTarget, String outputName) throws IOException;

    /**
     * returns the tags that are used to mark classes and methods as platform-specific. If a class or method is marked with any of these tags, it is not included in the compilation result, and is not analyzed for dependencies. This is used to exclude classes and methods that are not supported by the target platform, or that are implemented in a different way on the target platform.
     */
    String[] getPlatformTags();

    /**
     * called by {@link TeaVM} to check if the target supports async methods. If the target does not support async methods, the compiler will not generate async code, and will instead generate code that blocks the thread until the result is available. This is used to support targets that do not have native support for async methods, or that have a different model of concurrency.
     */
    boolean isAsyncSupported();

    default InliningFilterFactory getInliningFilter() {
        return InliningFilterFactory.DEFAULT;
    }

    /**
     * {@link TeaVM} will add all methods returned by this method as class initializers. This is used to support targets that have a different model of class initialization, or that have a different set of methods that can be used as class initializers. The target should return null if it does not want to add any methods as class initializers.
     */
    default Collection<? extends MethodReference> getInitializerMethods() {
        return null;
    }

    default boolean needsSystemArrayCopyOptimization() {
        return true;
    }

    /**
     * called by {@link TeaVM} to check if the target supports a given method as a class initializer. If the target does not support the method as a class initializer, the compiler will not generate code that calls the method as a class initializer, and will instead generate code that calls the method as a regular method. This is used to support targets that have a different model of class initialization, or that have a different set of methods that can be used as class initializers.
     */
    default boolean filterClassInitializer(String initializer) {
        return true;
    }
}
