/*
 *  Copyright 2012 Alexey Andreev.
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
package org.teavm.model;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.teavm.runtime.reflect.ModifiersInfo;

/**
 * Represents flags for classes and class members.
 * @see ElementHolder
 * @see AccessLevel
 * @author Alexey Andreev
 */
public enum ElementModifier {
    // the reference, in `ModifiersInfo` :

    // public static final int PUBLIC = 1;
    // public static final int PRIVATE = 1 << 1;
    // public static final int PROTECTED = 1 << 2;
    // public static final int STATIC = 1 << 3;
    // public static final int FINAL = 1 << 4;
    // public static final int SYNCHRONIZED = 1 << 5;
    // public static final int VOLATILE = 1 << 6;
    // public static final int TRANSIENT = 1 << 7;
    // public static final int NATIVE = 1 << 8;
    // public static final int INTERFACE = 1 << 9;
    // public static final int ABSTRACT = 1 << 10;
    // public static final int STRICT = 1 << 11;

    // public static final int JVM_FLAGS_MASK = (1 << 12) - 1;

    // public static final int VARARGS = 1 << 12;
    // public static final int ANNOTATION = 1 << 13;
    // public static final int INHERITED_ANNOTATION = 1 << 14;
    // public static final int SYNTHETIC = 1 << 15;
    // public static final int ENUM = 1 << 16;
    // public static final int BRIDGE = 1 << 17;
    // public static final int SIGNATUREPOLYMORPHIC = 1 << 19;

    ABSTRACT,
    INTERFACE,
    FINAL,
    ENUM,
    ANNOTATION,
    SYNTHETIC,
    BRIDGE,
    DEPRECATED,
    NATIVE,
    STATIC,
    STRICT,
    SYNCHRONIZED,
    TRANSIENT,
    VARARGS,
    VOLATILE,
    RECORD;

    /**
     * <p> does `access` signify signature polymorphic method?
     * 
     * <p> calls {@link ModifiersInfo#dictatesSignaturePolymorphism(int)}.
     * 
     * <p> since ASM (`org.ow2:asm`) strips the signature-polymorphic modifier bit from method access flags, we also treat methods marked as both `native` and `varargs` as signature-polymorphic, as this is the only known combination of modifiers that can be used to mark a method as signature-polymorphic in bytecode, and is used by the JDK for `java.lang.invoke.MethodHandle.invokeExact` and `java.lang.invoke.MethodHandle.invoke` methods.
     * 
     */
    public static boolean dictatesSignaturePolymorphism(int access) {
        return ModifiersInfo.dictatesSignaturePolymorphism(access) ;
    }

    public static int pack(Set<ElementModifier> elementModifiers) {
        ElementModifier[] knownModifiers = ElementModifier.values();
        int value = 0;
        int bit = 1;
        for (int i = 0; i < knownModifiers.length; ++i) {
            ElementModifier modifier = knownModifiers[i];
            if (elementModifiers.contains(modifier)) {
                value |= bit;
            }
            bit <<= 1;
        }
        return value;
    }

    public static int encodeModifiers(ClassReader cls) {
        var modifiers = asModifiersInfo(cls.readModifiers(), cls.getLevel());
        if (cls.hasModifier(ElementModifier.ANNOTATION)) {
            var retention = cls.getAnnotations().get(Retention.class.getName());
            if (retention != null && retention.getValue("value").getEnumValue().getFieldName().equals("RUNTIME")) {
                if (cls.getAnnotations().get(Inherited.class.getName()) != null) {
                    modifiers |= ModifiersInfo.INHERITED_ANNOTATION;
                }
            }
        }
        return modifiers;
    }

    public static int asModifiersInfo(Set<ElementModifier> elementModifiers, AccessLevel level) {
        var modifiers = 0;
        switch (level) {
            case PACKAGE_PRIVATE:
                break;
            case PRIVATE:
                modifiers |= ModifiersInfo.PRIVATE;
                break;
            case PROTECTED:
                modifiers |= ModifiersInfo.PROTECTED;
                break;
            case PUBLIC:
                modifiers |= ModifiersInfo.PUBLIC;
                break;
        }
        if (elementModifiers.contains(ElementModifier.STATIC)) {
            modifiers |= ModifiersInfo.STATIC;
        }
        if (elementModifiers.contains(ElementModifier.BRIDGE)) {
            modifiers |= ModifiersInfo.BRIDGE;
        }
        if (elementModifiers.contains(ElementModifier.FINAL)) {
            modifiers |= ModifiersInfo.FINAL;
        }
        if (elementModifiers.contains(ElementModifier.SYNCHRONIZED)) {
            modifiers |= ModifiersInfo.SYNCHRONIZED;
        }
        if (elementModifiers.contains(ElementModifier.VOLATILE)) {
            modifiers |= ModifiersInfo.VOLATILE;
        }
        if (elementModifiers.contains(ElementModifier.TRANSIENT)) {
            modifiers |= ModifiersInfo.TRANSIENT;
        }
        if (elementModifiers.contains(ElementModifier.NATIVE)) {
            modifiers |= ModifiersInfo.NATIVE;
        }
        if (elementModifiers.contains(ElementModifier.INTERFACE)) {
            modifiers |= ModifiersInfo.INTERFACE;
        }
        if (elementModifiers.contains(ElementModifier.ABSTRACT)) {
            modifiers |= ModifiersInfo.ABSTRACT;
        }
        if (elementModifiers.contains(ElementModifier.STRICT)) {
            modifiers |= ModifiersInfo.STRICT;
        }
        if (elementModifiers.contains(ElementModifier.VARARGS)) {
            modifiers |= ModifiersInfo.VARARGS;
        }
        if (elementModifiers.contains(ElementModifier.ANNOTATION)) {
            modifiers |= ModifiersInfo.ANNOTATION;
        }
        if (elementModifiers.contains(ElementModifier.SYNTHETIC)) {
            modifiers |= ModifiersInfo.SYNTHETIC;
        }
        if (elementModifiers.contains(ElementModifier.ENUM)) {
            modifiers |= ModifiersInfo.ENUM;
        }
        return modifiers;
    }
}
