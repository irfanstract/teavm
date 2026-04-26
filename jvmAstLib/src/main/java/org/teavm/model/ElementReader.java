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

import java.util.EnumSet;

/**
 * 
 * <p> defines methods to retrieve information/stats of the element.
 * subclasses of this interface are {@link ClassReader}, {@link MethodReader} and {@link FieldReader}.
 * each of these interfaces defines additional methods to read specific information about the element.
 * for example, {@link ClassReader} defines methods to read parent class, interfaces, methods and fields of the class.
 * {@link MethodReader} defines methods to read method parameters and return type.
 * {@link FieldReader} defines methods to read field type.
 * 
 * <p> this interface is used to read class, method and field information from class files and from cache.
 * it is also used to read class, method and field information from annotations.
 * 
 */
public interface ElementReader {
    AccessLevel getLevel();

    EnumSet<ElementModifier> readModifiers();

    boolean hasModifier(ElementModifier modifier);

    String getName();

    AnnotationContainerReader getAnnotations();
}
