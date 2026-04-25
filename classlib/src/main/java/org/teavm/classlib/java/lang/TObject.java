/*
 *  Copyright 2013 Alexey Andreev.
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
package org.teavm.classlib.java.lang;

import java.util.ArrayDeque;
import java.util.Queue;
import org.teavm.classlib.impl.ExtensionPhase;
import org.teavm.classlib.java.lang.invoke.TIntrinsics;
// import org.teavm.classlib.PlatformDetector;
// import org.teavm.backend.wasm.runtime.gc.WasmGCSupport;
// import org.teavm.dependency.PluggableDependency;
// import org.teavm.interop.Address;
// import org.teavm.interop.Async;
// import org.teavm.interop.AsyncCallback;
// import org.teavm.interop.DelegateTo;
import org.teavm.interop.NoSideEffects;
import org.teavm.interop.Rename;
// import org.teavm.interop.Structure;
// import org.teavm.interop.Superclass;
// import org.teavm.interop.Unmanaged;
// import org.teavm.jso.browser.TimerHandler;
// import org.teavm.platform.Platform;
// import org.teavm.platform.PlatformObject;
// import org.teavm.platform.PlatformRunnable;
// import org.teavm.runtime.Allocator;
// import org.teavm.runtime.EventQueue;
// import org.teavm.runtime.RuntimeArray;
// import org.teavm.runtime.RuntimeClass;
// import org.teavm.runtime.RuntimeObject;
// import org.teavm.runtime.reflect.ClassInfo;

@org.teavm.classlib.implAnnotation.ExtensionPhase(ExtensionPhase.COMPLEMENTING )
public class TObject {

    @Rename("getClass")
    final Class<?> getClass0() {
        return TIntrinsics.apply();
    }

    @Override
    public int hashCode() {
        return identity();
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(identity());
    }

    private String obfuscatedToString() {
        return "<java_object>@" + Integer.toHexString(identity());
    }

    final int identity() {
        return TIntrinsics.apply();
    }

    @Rename("clone")
    private native TObject cloneObject();

    @Rename("notify")
    public final void notify0() {
        TIntrinsics.apply();
    }

    @Rename("notifyAll")
    public final void notifyAll0() {
        TIntrinsics.apply();
    }

    @Rename("wait")
    public final void wait0(long timeout) throws TInterruptedException {
        try {
            wait(timeout, 0);
        } catch (InterruptedException ex) {
            throw new TInterruptedException();
        }
    }

    @Rename("wait")
    private void wait0(long timeout, int nanos) throws TInterruptedException {
        TIntrinsics.apply();
    }

    @Rename("wait")
    final void wait0() throws InterruptedException {
        try {
            wait(0L);
        } catch (InterruptedException ex) {
            throw ex;
        }
    }

}


