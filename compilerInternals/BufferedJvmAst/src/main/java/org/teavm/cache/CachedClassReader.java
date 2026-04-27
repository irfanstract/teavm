/*
 *  Copyright 2019 Alexey Andreev.
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
package org.teavm.cache;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.teavm.model.ClassReader;
import org.teavm.model.FieldReader;
import org.teavm.model.GenericTypeParameter;
import org.teavm.model.GenericValueType;
import org.teavm.model.MethodDescriptor;
import org.teavm.model.MethodReader;

/**
 * <p> cached class reader. subclasses {@link CachedElement} and implements {@link ClassReader}.
 * it is used to store information about classes in cache, so it can be retrieved faster than reading from class files.
 * it is also used to read information about classes from annotations.
 * 
 * <p> instantiated by {@link ClassIO#readClass(java.io.InputStream, String)} when reading class information from class files or annotations. it stores information about class, such as parent class, interfaces, methods and fields.
 * 
 */
class CachedClassReader extends CachedElement implements ClassReader {
    String parent;
    GenericTypeParameter[] parameters;
    GenericValueType.Object genericParent;
    String owner;
    String declaringClass;
    String simpleName;
    Set<String> interfaces;
    Set<GenericValueType.Object> genericInterfaces;
    Map<MethodDescriptor, CachedMethod> methods;
    Map<String, CachedField> fields;
    List<String> innerClasses = new ArrayList<>();

    @Override
    public GenericTypeParameter[] getGenericParameters() {
        return parameters != null ? parameters.clone() : new GenericTypeParameter[0];
    }

    @Override
    public String getParent() {
        return parent;
    }

    @Override
    public GenericValueType.Object getGenericParent() {
        return genericParent;
    }

    @Override
    public Set<String> getInterfaces() {
        return interfaces;
    }

    @Override
    public Set<GenericValueType.Object> getGenericInterfaces() {
        return genericInterfaces;
    }

    @Override
    public MethodReader getRawMethod(MethodDescriptor method) {
        return methods.get(method);
    }

    // @Override
    // public MethodReader getMethod(MethodDescriptor que) {
    //     var result = (
    //         methods.entrySet().stream()
    //         .filter(met -> met.getKey().getName() == que.getName())
    //         .map(e -> e.getValue())
    //         .toList()
    //     );
    //     if (result.isEmpty()) {
    //         return null;
    //     }
    //     if (1 < result.size()) {
    //         return null;
    //     }
    //     {
    //     var m = result.get(0) ;
    //     if (m.isSignaturePolymorphic() ) {
    //         // TODO
    //         throw new Error(MessageFormat.format("searching {0} {1} got SP-y {2}", que.getName(), List.of(que.getParameterTypes()), List.of(m.getParameterTypes()) ) ) ;
    //     } else {
    //         return m ;
    //     }
    //     }
    // }

    @Override
    public Collection<? extends MethodReader> getMethods() {
        return List.copyOf(methods.values());
    }

    @Override
    public FieldReader getField(String name) {
        return fields.get(name);
    }

    @Override
    public Collection<? extends FieldReader> getFields() {
        return List.copyOf(fields.values());
    }

    @Override
    public String getOwnerName() {
        return owner;
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public String getDeclaringClassName() {
        return declaringClass;
    }

    @Override
    public List<? extends String> getInnerClasses() {
        return innerClasses;
    }
}
