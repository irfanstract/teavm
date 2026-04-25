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
package org.teavm.parsing;

import java.util.Date;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderSource;
import org.teavm.model.ReferenceCache;
import org.teavm.parsing.resource.MapperClassHolderSource;
import org.teavm.parsing.resource.ResourceClassHolderMapper;
import org.teavm.parsing.resource.ResourceProvider;

public class ClasspathClassHolderSource implements ClassHolderSource, ClassDateProvider {
    private MapperClassHolderSource innerClassSource;
    private RenamingResourceMapper classPathMapper;

    public ClasspathClassHolderSource(ResourceProvider resourceProvider, ReferenceCache referenceCache) {
        ResourceClassHolderMapper rawMapper = new ResourceClassHolderMapper(resourceProvider, referenceCache);
        classPathMapper = new RenamingResourceMapper(resourceProvider, referenceCache, rawMapper);
        innerClassSource = new MapperClassHolderSource(classPathMapper);
    }

    public ClasspathClassHolderSource(ClassLoader classLoader, ReferenceCache referenceCache) {
        this(new ClasspathResourceProvider(classLoader ), referenceCache);
    }

    /**
     * 
     * @deprecated
     * this overload sets, as `resourceProvider`, an implementation backed by a {@link ClassLoader} chosen arbitrarily (because there's not currently a known reliable means/heuristics to determine which one the most appropriate one is), which can lead to linking failures or even silently linking to wrong classpaths.
     * Use the overload that takes a {@link ResourceProvider} instead, and provide an explicit {@link ResourceProvider} implementation that uses the desired {@link ClassLoader}.
     * 
     * <p> <code>@konsoletyper</code>'s TeaVM sets this to `ClasspathClassHolderSource.class.getClassLoader()`.
     * unfortunately, that won't work on, say, JPMS, OSGi, or Android.
     */
    @Deprecated
    public ClasspathClassHolderSource(ReferenceCache referenceCache) {
        this(Thread.currentThread().getContextClassLoader(), referenceCache);
    }

    @Override
    public ClassHolder get(String name) {
        return innerClassSource.get(name);
    }

    @Override
    public Date getModificationDate(String className) {
        return classPathMapper.getModificationDate(className);
    }
}
