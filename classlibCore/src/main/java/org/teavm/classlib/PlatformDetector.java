package org.teavm.classlib;

public final class PlatformDetector {
    private PlatformDetector() { }

    public static boolean isWebAssemblyGC() {
        return false;
    }

    public static boolean isC() {
        return false;
    }
}
