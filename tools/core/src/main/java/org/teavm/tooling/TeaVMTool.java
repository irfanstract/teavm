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
package org.teavm.tooling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.teavm.backend.javascript.JSModuleType;
import org.teavm.cache.AlwaysStaleCacheStatus;
import org.teavm.cache.CacheStatus;
import org.teavm.cache.DiskCachedClassReaderSource;
import org.teavm.cache.DiskMethodNodeCache;
import org.teavm.cache.DiskProgramCache;
import org.teavm.cache.EmptyProgramCache;
import org.teavm.cache.FileSymbolTable;
import org.teavm.debugging.information.DebugInformation;
import org.teavm.debugging.information.DebugInformationBuilder;
import org.teavm.debugging.information.SourceMapsWriter;
import org.teavm.model.ClassHolderTransformer;
import org.teavm.model.ClassReader;
import org.teavm.model.PreOptimizingClassHolderSource;
import org.teavm.model.ReferenceCache;
import org.teavm.parsing.ClasspathClassHolderSource;
import org.teavm.parsing.ClasspathResourceProvider;
import org.teavm.parsing.resource.ResourceProvider;
import org.teavm.tooling.sources.DefaultSourceFileResolver;
import org.teavm.tooling.sources.SourceFileProvider;
import org.teavm.vm.BuildTarget;
import org.teavm.vm.DirectoryBuildTarget;
import org.teavm.vm.TeaVM;
import org.teavm.vm.TeaVMBuilder;
import org.teavm.vm.TeaVMOptimizationLevel;
import org.teavm.vm.TeaVMProgressListener;
import org.teavm.vm.TeaVMTarget;

public class TeaVMTool {
    private File targetDirectory = new File(".");
    private TeaVMTargetType targetType = TeaVMTargetType.JAVASCRIPT;
    private String targetFileName = "";
    private boolean obfuscated = true;
    private JSModuleType jsModuleType = JSModuleType.UMD;
    private boolean strict;
    private int maxTopLevelNames = 80_000;
    private String mainClass;
    private String entryPointName = "main";
    private Properties properties = new Properties();
    private boolean debugInformationGenerated;
    private boolean sourceMapsFileGenerated;
    private TeaVMSourceFilePolicy sourceFilePolicy = TeaVMSourceFilePolicy.DO_NOTHING;
    private boolean incremental;
    private File cacheDirectory = new File("./teavm-cache");
    private List<String> transformers = List.of();
    private List<String> classesToPreserve = List.of();
    private TeaVMToolLog log = new EmptyTeaVMToolLog();
    private ClassLoader classLoader = TeaVMTool.class.getClassLoader();
    private List<File> classPath;
    private boolean importedWasmMemory;
    private ReferenceCache referenceCache;
    private boolean heapDump;
    private boolean shortFileNames;
    private boolean assertionsRemoved;

    private List<ClassHolderTransformer> resolveTransformers() {
        List<ClassHolderTransformer> transformerInstances = List.of();
        if (transformers == null) {
            return transformerInstances;
        }
        return transformerInstances;
    }
}


