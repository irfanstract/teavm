package org.teavm.vm;

import org.teavm.vm.spi.TeaVMPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

// public final class TeaVMPluginLoader {
//     private TeaVMPluginLoader() { }

//     /**
//      * 
//      */
//     @Deprecated
//     public static Iterable<TeaVMPlugin> load(ClassLoader classLoader) {
//         return loadThroughServiceLoaderApi(classLoader);
//         // return List.of((TeaVMPlugin) Class.forName("org.teavm.classlib.impl.JCLPlugin").newInstance() );
//     }

//     /**
//      * 
//      * @deprecated
//      * we're in the process of getting rid of ServiceLoader-based traversal since
//      * such a mechanism cannot be made sufficiently flexible for most use-case(s) onwards.
//      */
//     @Deprecated
//     public static Iterable<TeaVMPlugin> loadThroughServiceLoaderApi(ClassLoader classLoader) {
//         List<TeaVMPlugin> result = new ArrayList<>();
//         ServiceLoader<TeaVMPlugin> loader = ServiceLoader.load(TeaVMPlugin.class, classLoader);
//         for (TeaVMPlugin plugin : loader) {
//             result.add(plugin);
//         }
//         return result;
//     }
// }
