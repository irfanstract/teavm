package org.teavm
package vm

import org.teavm.vm.spi.TeaVMPlugin

// import java.util.ArrayList;
// import java.util.List;
import java.util.ServiceLoader


import language.unsafeNulls
import scala.jdk.CollectionConverters.{*, given}

object TeaVMPluginLoader :

    /**
     * 
     */
    def load(classLoader: ClassLoader): java.lang.Iterable[TeaVMPlugin] =
        // loadThroughServiceLoaderApi(classLoader)
        locally :
            locally :
                Class.forName("org.teavm.classlib.impl.JCLPlugin", true, Thread.currentThread().getContextClassLoader() ).newInstance()
                .asInstanceOf[TeaVMPlugin]
            .pipe { Seq(_) }
            .asJava

