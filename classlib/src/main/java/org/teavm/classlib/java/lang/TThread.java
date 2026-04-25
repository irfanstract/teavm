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
package org.teavm.classlib.java.lang;

import org.teavm.classlib.PlatformDetector;
import org.teavm.classlib.impl.ExtensionPhase;
import org.teavm.classlib.java.lang.invoke.TIntrinsics;
import org.teavm.interop.Async;
import org.teavm.interop.AsyncCallback;
// import org.teavm.platform.Platform;
// import org.teavm.platform.PlatformRunnable;
import org.teavm.runtime.EventQueue;
import org.teavm.runtime.Fiber;

@org.teavm.classlib.implAnnotation.ExtensionPhase(ExtensionPhase.COMPLEMENTING )
public class TThread {

    public void start() {
        TIntrinsics.apply();
    }

    public static TThread currentThread() {
        return TIntrinsics.apply();
    }

    public String getName() {
        return TIntrinsics.apply();
    }

    public void setName(String name) {
        TIntrinsics.apply();
    }

    public final boolean isDaemon() {
        return TIntrinsics.apply();
    }

    public final void setDaemon(boolean daemon) {
        TIntrinsics.apply();
    }

    public final void join(long millis, int nanos) throws InterruptedException {
        TIntrinsics.apply();
    }

    public final void join(long millis) throws InterruptedException {
        join(millis, 0);
    }

    public final void join() throws InterruptedException {
        join(0);
    }

    public static void yield() {
        TIntrinsics.apply();
    }

    public void interrupt() {
        TIntrinsics.apply();
    }

    public static boolean interrupted() {
        return TIntrinsics.apply();
    }

    public boolean isInterrupted() {
        return TIntrinsics.apply();
    }

    public boolean isAlive() {
        return TIntrinsics.apply();
    }

    public static int activeCount() {
        return TIntrinsics.apply();
    }

    public long getId() {
        return TIntrinsics.apply();
    }

    public static boolean holdsLock(TObject obj) {
        return TIntrinsics.apply();
    }

    @Async
    public static native void sleep(long millis) throws TInterruptedException;

    public final void setPriority(int newPriority) {
        TIntrinsics.apply();
    }

    public final int getPriority() {
        return TIntrinsics.apply();
    }

    public StackTraceElement[] getStackTrace() {
        return TIntrinsics.apply();
    }

    public ClassLoader getContextClassLoader() {
        return TIntrinsics.apply();
    }
    
}





