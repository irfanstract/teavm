
package org.teavm

package parsing




import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

// import org.teavm.backend.javascript.JavaScriptTarget;
// import org.teavm.model.ClassHolder;
// import org.teavm.model.ClassHolderSource;
// import org.teavm.parsing.ClasspathClassHolderSource;
// import org.teavm.parsing.ClasspathResourceProvider;
// import org.teavm.parsing.TransformedResourceProvider1;
import org.teavm.parsing.resource.Resource
import org.teavm.parsing.resource.ConstResource

import org.objectweb.{asm}



import language.unsafeNulls

import scala.jdk.CollectionConverters.{given}

object AsmTxUtil :

    object Prep :

        /**
         * parse the classfile, return the ClassNode.
         */
        def parseClassFile(cls: Resource )
        =
            asm.tree.ClassNode()
            .tap : c =>
                new asm.ClassReader(cls.open().readAllBytes() )
                .accept(c, 0 )







