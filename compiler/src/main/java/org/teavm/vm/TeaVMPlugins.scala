
package org.teavm
package vm

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Map;
// import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;
// import org.teavm.metaprogramming.CompileTime;
import org.teavm.parsing.AsmUtil;
import org.teavm.vm.spi.After;
import org.teavm.vm.spi.Before;
import org.teavm.vm.spi.Requires;

import language.unsafeNulls
import scala.jdk.CollectionConverters.{given}

object TeaVMPlugins {

    final val DESCRIPTOR_LOCATION = "META-INF/services/org.teavm.vm.spi.TeaVMPlugin";
    final val REQUIRES_DESC = Type.getDescriptor(classOf[Requires]);
    final val   BEFORE_DESC = Type.getDescriptor(classOf[Before  ]);
    final val    AFTER_DESC = Type.getDescriptor(classOf[After   ]);

} // TeaVMPlugins

object TeaVMPluginListFile {

    /**
     * read the lines from the given `input`, ignoring empty lines and lines starting with `#`.
     * 
     */
    def readLineByLine(input: BufferedReader )
        : Set[String]
    =
        collection.mutable.Set.empty[String]
        .tap { readLineByLineIntoSet(input, _ ) }
        .toSet

    def readLineByLineIntoSet(input: BufferedReader, dest: java.util.Set[String] ): Unit =
        readLineByLineIntoSet(input, dest.asScala )

    /**
     * read the lines from the given `input` and add them to the given `dest` set, ignoring empty lines and lines starting with `#`.
     * The input is expected to be in UTF-8 encoding.
     * 
     */
    def readLineByLineIntoSet(input: BufferedReader, dest: collection.mutable.Set[String] ): Unit =
        while (true) do
            val line = input.readLine()
            if (line == null) then
                return

            val line2 = line.trim()
            if (line2.isEmpty() || line2.startsWith("#") ) then {
                // skip
            } else
                dest += line2

    // static void readPlugins(BufferedReader input, Set<String> plugins) throws IOException {
    //     while (true) {
    //         String line = input.readLine();
    //         if (line == null) {
    //             break;
    //         }
    //         line = line.trim();
    //         if (line.isEmpty() || line.startsWith("#")) {
    //             continue;
    //         }

    //         plugins.add(line);
    //     }
    // }

} // TeaVMPluginList

object TeaVMPluginResolution1 {

    case object IT_DID

} // TeaVMPluginResolution1

private
object TeaVMPluginSysUtil :

    // @throws[java.io.IOException]
    def loadTextResource(classLoader: ClassLoader , DESCRIPTOR_LOCATION: String, accept: Consumer[java.io.BufferedReader] )
    =
        {
            // var resourceFiles = classLoader.getResources(DESCRIPTOR_LOCATION);
            // while (resourceFiles.hasMoreElements()) {
            //     URL resourceFile = resourceFiles.nextElement();
            //     try (var input = new BufferedReader(
            //             new InputStreamReader(resourceFile.openStream(), StandardCharsets.UTF_8))) {
            //         readPlugins(input, unorderedPlugins);
            //     }
            // }
            val resources = classLoader.getResources(DESCRIPTOR_LOCATION).asScala.toSeq
            for (resource <- resources ) do
                resource.openStream()
                .pipe { is => new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8 ) ) }
                .tap { br => accept.accept(br) }
        }






