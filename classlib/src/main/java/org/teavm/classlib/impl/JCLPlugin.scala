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

    override def install(ctx: TeaVMHost) = {

        if (!isBootstrap()) {
            ???
        }

        ???

    }

    // private def installMetadata(reg: Me): Unit = {
    //     // TBD
    // }

object JCLPlugin {

    @PlatformMarker
    def isBootstrap(): Boolean = {
        false
    }
}



