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
package org.teavm.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.teavm.common.Sets;

/**
 * <p> specifies method name and signature. Signature is an array of types, where the last type is a return type, and the rest are parameter types. </p>
 * 
 * @implNote
 * 
 * TODO: erased types as used in 'type descriptor's, are conventionally called "type descriptor"s; please reword
 * 
 * TODO: consider turning this into `record`. thought about making such change, but {@link #MethodDescriptor(String, ValueType...) <code>signature</code> being varargs} got me backing off
 * 
 */
public record MethodDescriptor(String name, List<ValueType> paramTyps, ValueType retType) implements Serializable {

    public MethodDescriptor(String name, ValueType[] paramTyps, ValueType retType) {
        this(name, List.of(paramTyps), retType );
    }

    /**
     * @deprecated please specify 'params' and 'return' separately.
     */
    @Deprecated
    public MethodDescriptor(String name, List<ValueType> paramTypesAndReturnType) {
        this(name, paramTypesAndReturnType.subList(0, paramTypesAndReturnType.size() + -1 ), paramTypesAndReturnType.get(paramTypesAndReturnType.size() + -1 ) );
        if (paramTypesAndReturnType.size() < 1) {
            throw new IllegalArgumentException("Signature must be at least 1 element length");
        }
    }

    /**
     * @deprecated please specify 'params' and 'return' separately.
     */
    @Deprecated
    public MethodDescriptor(/* String */ CharSequence name, ValueType... signature) {
        this(name.toString(), List.of(signature));
    }

    /**
     * @deprecated please specify 'params' and 'return' separately.
     */
    @Deprecated
    public MethodDescriptor(String name, Class<?>... signature) {
        this(name, List.of(signature).stream().map(ValueType::parse).toList() );
    }

    public String getName() {
        return name;
    }

    public ValueType[] getSignature() {
        return Sets.appended(paramTyps, retType).toArray(new ValueType[0]);
    }

    public ValueType[] getParameterTypes() {
        return paramTyps.toArray(new ValueType[0]);
    }

    public ValueType getResultType() {
        return retType;
    }

    public int parameterCount() {
        return paramTyps.size();
    }

    public ValueType parameterType(int index) {
        return paramTyps.get(index);
    }

    @Override
    public String toString() {
        return name + signatureToString();
    }

    public String signatureToString() {
        var signature = getSignature();

        StringBuilder sb = new StringBuilder();
        sb.append('(');
        for (int i = 0; i < signature.length - 1; ++i) {
            sb.append(signature[i].toString());
        }
        sb.append(')');
        sb.append(signature[signature.length - 1]);
        return sb.toString();
    }

    // public static MethodDescriptor get(MethodHolder method) {
    //     return new MethodDescriptor(method.getName(), method.getSignature());
    // }

    public static MethodDescriptor parse(String text) {
        MethodDescriptor desc = parseIfPossible(text);
        if (desc == null) {
            throw new IllegalArgumentException("Wrong method descriptor: " + text);
        }
        return desc;
    }

    public static MethodDescriptor parseIfPossible(String text) {
        int parenIndex = text.indexOf('(');
        if (parenIndex < 1) {
            return null;
        }
        return new MethodDescriptor(text.substring(0, parenIndex),
                parseSignature(text.substring(parenIndex)));
    }

    public static ValueType[] parseSignature(String text) {
        ValueType[] signature = parseSignatureIfPossible(text);
        if (signature == null) {
            throw new IllegalArgumentException("Illegal method signature: " + text);
        }
        return signature;
    }

    public static ValueType[] parseSignatureIfPossible(String text) {
        if (text.charAt(0) != '(') {
            return null;
        }
        int index = text.indexOf(')', 1);
        if (index <= 0) {
            return null;
        }
        ValueType[] params = ValueType.parseManyIfPossible(text.substring(1, index));
        if (params == null) {
            return null;
        }
        ValueType result = ValueType.parseIfPossible(text.substring(index + 1));
        if (result == null) {
            return null;
        }
        ValueType[] signature = new ValueType[params.length + 1];
        System.arraycopy(params, 0, signature, 0, params.length);
        signature[params.length] = result;
        return signature;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        if (hash == 0) {
            hash = name.hashCode();
            for (ValueType param : getSignature()) {
                hash = 31 * hash + param.hashCode();
            }
            if (hash == 0) {
                hash++;
            }
        }

        return hash;
    }

}
