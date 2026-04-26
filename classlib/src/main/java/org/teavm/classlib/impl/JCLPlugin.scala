/*
 *  Copyright 2026 The IXTVM Project.
 *
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

package classlib
package impl

// import java.lang.reflect.Array;
// import java.util.ServiceLoader;
import org.teavm.interop.PlatformMarker;
import org.teavm.model.MethodReference;
import org.teavm.model.ValueType;
import org.teavm.runtime.reflect.ClassInfo;
// import org.teavm.vm.TeaVMPluginUtil;
import org.teavm.vm.spi.After;
import org.teavm.vm.spi.TeaVMHost;
import org.teavm.vm.spi.TeaVMPlugin;
import org.teavm.dependency.DependencyAnalyzer

val _ = { }





class JCLPlugin extends TeaVMPlugin :

    import JCLPlugin.{isBootstrap}

    override def install(host: TeaVMHost) = {
        // ???

        // if (!isBootstrap()) {
        // //     ServiceLoaderSupport serviceLoaderSupport = new ServiceLoaderSupport(host.getClassLoader(),
        // //             host.getResourceProvider());
        // //     host.add(serviceLoaderSupport);
        // //     host.registerService(ServiceLoaderInformation.class, serviceLoaderSupport);
        // //     MethodReference loadServicesMethod = new MethodReference(ServiceLoader.class, "loadServices",
        // //             ClassInfo.class, Object[].class);

        // //     TeaVMJavaScriptHost jsExtension = host.getExtension(TeaVMJavaScriptHost.class);
        // //     if (jsExtension != null) {
        // //         jsExtension.add(loadServicesMethod, new ServiceLoaderJSSupport());
        // //         jsExtension.addVirtualMethods(new AnnotationVirtualMethods());
        // //     }

        // //     TeaVMCHost cHost = host.getExtension(TeaVMCHost.class);
        // //     if (cHost != null) {
        // //         cHost.addGenerator(new ServiceLoaderCSupport());
        // //     }

        // //     var wasmHost = host.getExtension(TeaVMWasmHost.class);
        // //     if (wasmHost != null) {
        // //         wasmHost.add(new ServiceLoaderWasmSupport());
        // //     }

        // //     var wasmGCHost = host.getExtension(TeaVMWasmGCHost.class);
        // //     if (wasmGCHost != null) {
        // //         wasmGCHost.addGeneratorFactory(new ServiceLoaderWasmGCSupport());
        // //     }
        //     ???
        // }

        // if (!isBootstrap()) {
        //     ???
        // }

        // if (!isBootstrap()) {
        //     //     host.add(new ScalaHacks());
        //     //     host.add(new KotlinHacks());
        //     ???
        // }

        // ???

        // if (!isBootstrap()) {
        //     //     TeaVMCHost cHost = host.getExtension(TeaVMCHost.class);
        //     //     if (cHost != null) {
        //     //         cHost.addIntrinsic(context -> new DateTimeZoneProviderIntrinsic(context.getProperties()));
        //     //     }

        //     //     TeaVMWasmHost wasmHost = host.getExtension(TeaVMWasmHost.class);
        //     //     if (wasmHost != null) {
        //     //         wasmHost.add(context -> new DateTimeZoneProviderIntrinsic(context.getProperties()));
        //     //     }
        //     ???
        // }

        // if (!isBootstrap()) {
        //     //     TeaVMPluginUtil.handleNatives(host, Class.class);
        //     //     TeaVMPluginUtil.handleNatives(host, ClassLoader.class);
        //     //     TeaVMPluginUtil.handleNatives(host, System.class);
        //     //     TeaVMPluginUtil.handleNatives(host, Array.class);
        //     //     TeaVMPluginUtil.handleNatives(host, Math.class);
        // }

        // ???

        // ???

        // ???

        // ???
        // if (false) {
        //     // host.add(new JSStringTransformer());
        //     // js.addInjectorProvider(new JSStringInjector());
        //     // js.add(new MethodReference(String.class, "<init>", Object.class, void.class),
        //     //         new JSStringConstructorGenerator());
        // } else {
        //     // host.add(new DefaultStringTransformer());
        // }

        {
            try
                JCLPlugin.cissClass.getMethod("applyTo", classOf[TeaVMHost] )
            catch case z: NoSuchMethodException =>
                throw new RuntimeException(s"methods: ${for { m <- JCLPlugin.cissClass.getDeclaredMethods().toSeq } yield m.toGenericString() }")
        }
        .invoke(null, host )
    }

    // private def installMetadata(reg: Me): Unit = {
    //     // TBD
    // }

object JCLPlugin {

    lazy val cissClass = Class.forName("org.teavm.classlib.impl.CommonIndySubstSetup")

    @PlatformMarker
    def isBootstrap(): Boolean = {
        false
    }
}



