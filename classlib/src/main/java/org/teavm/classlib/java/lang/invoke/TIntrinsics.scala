
package org.teavm.classlib.java.lang.invoke

import org.teavm.classlib.PlatformDetector
import org.teavm.classlib.impl.ExtensionPhase
import org.teavm.interop.Async
import org.teavm.interop.AsyncCallback
// import org.teavm.platform.Platform
// import org.teavm.platform.PlatformRunnable
import org.teavm.runtime.EventQueue
import org.teavm.runtime.Fiber

// @org.teavm.classlib.implAnnotation.ExtensionPhase(ExtensionPhase.COMPLEMENTING )
object TIntrinsics {

    /**
     * as IXTVM preprocesses classfiles from StdLibPatches,
     * each method whose body simply makes `INVOKESTATIC` call to this method and then `*RETURN`s will be treated as if it were defined with `native`.
     * 
     * ```java
     * public void wait0(long timeout, int nanos);
     *  code:
     *   INVOKESTATIC org.teavm.classlib.java.lang.invoke.TIntrinsics#apply():Ljava/lang/Object;
     *   [AIJFDV]RETURN
     * ````
     * 
     */
    def apply[T](): T
    = { ??? }

}



