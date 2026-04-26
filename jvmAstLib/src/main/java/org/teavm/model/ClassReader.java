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
package org.teavm.model;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.teavm.cache.InmemInstantiatedSpMethodView;

/**
 * <p> a subclass of {@link ElementReader} for classes. It provides methods to read class-specific information, such as parent class, interfaces, methods and fields.
 * 
 */
public interface ClassReader extends ElementReader {
    GenericTypeParameter[] getGenericParameters();

    String getParent();

    Set<String> getInterfaces();

    GenericValueType.Object getGenericParent();

    Set<GenericValueType.Object> getGenericInterfaces();

    /**
     * <p> a method with given types, instantiating one in-case-of the method being signature-polymorphic.
     * 
     * <p> see also {@link #getRawMethod}.
     * 
     */
    default MethodReader getMethod(MethodDescriptor que) {
        {
        var m = getSignaturePolymorphicMethod(que, s -> null ) ;
        if (m != null ) {
            // TODO
            if (false) {
                System.err.println(MessageFormat.format("searching {0} {1} got SP-y {2}", que.getName(), List.of(que.getParameterTypes()), List.of(m.getParameterTypes()) ) ) ;
            }
            return InmemInstantiatedSpMethodView.wrap(m, que);
        }
        }
        return getRawMethod1(que) ;
    }

    /**
     * <p> the method, as defined in bytecode, ignoring the signature-polymorphic modifier bit even if present.
     * 
     * <p> see also {@link #getMethod(MethodDescriptor)}
     * 
     */
    default MethodReader getRawMethod(MethodDescriptor que) {
        return getRawMethod1(que) ;
    }

    /**
     * 
     * @return
     * the method with the same name as the given one, and marked as signature-polymorphic.
     * if there's no such method, null is returned.
     * if the method were overloaded, this will call the provided handler, and return its result. the default implementation of the handler returns null, which means that in case of overloads, the method will not be resolved as signature-polymorphic, and will be resolved as a regular method instead. an alternative implementation may throw an exception, or return one of the found methods (for example, the first one).
     */
    default MethodReader getSignaturePolymorphicMethod(MethodDescriptor que, BadSigPolymoOverloadingFoundHandler bspoh ) {
        var result = (
            getMethods().stream()
            .filter(met -> met.getName().equals(que.getName()) )
            .toList()
        );
        if (result.isEmpty()) {
            return null;
        }
        {
        if (result.size() == 1 ) {
        var m = result.get(0) ;
        if (m.isSignaturePolymorphic() ) {
            return m ;
        }
        } // fi
        return bspoh.apply(result) ;
        }
    }

    /**
     * <p> the fallback behaviour for the case when the resolver attempts instantiation of polymorphic signatures, yet were presented with overloads.
     * this should never happen, as such methods should be rejected by the Java compiler, but in case of malformed bytecode it may happen, and we need to have a way to handle it.
     * 
     * <p> the default implementation returns null, which means that the method will not be resolved as signature-polymorphic, and will be resolved as a regular method instead. if the method is not found as a regular method either, then it will not be resolved at all. an alternative implementation may throw an exception, or return one of the found methods (for example, the first one).
     * 
     */
    interface BadSigPolymoOverloadingFoundHandler {

        MethodReader apply(Collection<? extends MethodReader> s) ;

    } // BadSigPolymoOverloadingFoundHandler

    private MethodReader getRawMethod1(MethodDescriptor que) {
        return (
            getMethods().stream()
            .filter(met -> met.getDescriptor().equals(new MethodDescriptor(que.getName(), que.getParameterTypes(), que.getResultType() ) ) )
            .findFirst()
            .orElse(null)
        ) ;
    }

    Collection<? extends MethodReader> getMethods();

    FieldReader getField(String name);

    Collection<? extends FieldReader> getFields();

    String getOwnerName();

    String getSimpleName();

    String getDeclaringClassName();

    List<? extends String> getInnerClasses();
}
