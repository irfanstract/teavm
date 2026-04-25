/*
 *  Copyright 2026 Alexey Andreev.
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
package org.teavm.runtime.reflect;

import org.objectweb.asm.Opcodes;

public final class ModifiersInfo {
    private ModifiersInfo() {
    }

    public static final int PUBLIC = 1;
    public static final int PRIVATE = 1 << 1;
    public static final int PROTECTED = 1 << 2;
    public static final int STATIC = 1 << 3;
    public static final int FINAL = 1 << 4;
    public static final int SYNCHRONIZED = 1 << 5;
    public static final int VOLATILE = 1 << 6;
    public static final int TRANSIENT = 1 << 7;
    public static final int NATIVE = 1 << 8;
    public static final int INTERFACE = 1 << 9;
    public static final int ABSTRACT = 1 << 10;
    public static final int STRICT = 1 << 11;

    public static final int JVM_FLAGS_MASK = (1 << 12) - 1;

    public static final int VARARGS = 1 << 12;
    public static final int ANNOTATION = 1 << 13;
    public static final int INHERITED_ANNOTATION = 1 << 14;
    public static final int SYNTHETIC = 1 << 15;
    public static final int ENUM = 1 << 16;
    public static final int BRIDGE = 1 << 17;
    @Deprecated
    public static final int SIGNATUREPOLYMORPHIC = 1 << 19;

    /**
     * <p> does `access` signify signature polymorphic method?
     * 
     * <p> since ASM (`org.ow2:asm`) strips the signature-polymorphic modifier bit from method access flags, we also treat methods marked as both `native` and `varargs` as signature-polymorphic, as this is the only known combination of modifiers that can be used to mark a method as signature-polymorphic in bytecode, and is used by the JDK for `java.lang.invoke.MethodHandle.invokeExact` and `java.lang.invoke.MethodHandle.invoke` methods.
     * 
     */
    public static boolean dictatesSignaturePolymorphism(int access) {
        return (
            (access & 0x00080000) != 0
            ||
            /* WORKAROUND */ ((access & Opcodes.ACC_NATIVE) != 0 && (access & Opcodes.ACC_VARARGS) != 0 )
        ) ;
    }

}
