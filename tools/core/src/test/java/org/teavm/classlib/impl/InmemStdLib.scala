
package org.teavm

package classlib.impl





import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import org.teavm.backend.javascript.JavaScriptTarget;
import org.teavm.model.ClassHolder;
import org.teavm.model.ClassHolderSource;
import org.teavm.parsing.ClasspathClassHolderSource;
import org.teavm.parsing.ClasspathResourceProvider;
import org.teavm.parsing.TransformedResourceProvider1;
import org.teavm.parsing.resource.Resource
import org.teavm.parsing.resource.ConstResource

import org.objectweb.{asm}



import scala.jdk.CollectionConverters.{given}

object InmemStdLib {

    def transformClassfile(cls: Resource )
    =
        locally :
            val cr = new asm.ClassReader(cls.open().readAllBytes() )
            val cw = new asm.ClassWriter(cr, 0 )
            getClassVisitorTransformer()(cw)
            .pipe : c =>
                cr.accept(c, 0 )
            ConstResource(cw.toByteArray().pipe { IArray.unsafeFromArray(_) } )

    def getClassVisitorTransformer
        ()
        : asm.ClassVisitor => asm.ClassVisitor
    =
      v0 =>
        new asm.ClassVisitor(asm.Opcodes.ASM9, v0 ) { thisCv =>

            import language.unsafeNulls

            import asm.{*}

            override def visit(version: Int, access: Int, implName: (String) , signature: (String) , superName: (String) , interfaces: (Array[(String) ]) ): Unit
            =
                implName.pipe { demangleClassName(_) }
                .pipe: name =>

                    require(!(implName startsWith "org/teavm") || !(name startsWith "org/teavm") , s"name=$name, implName=$implName" )

                    if !(name matches "java/lang/(Object|String|Double|Float|Long|Int|Short|Char|Byte|Boolean|Number)" ) && (name startsWith "java/") then

                        // locally {
                        //             name
                        //             .replaceFirst("\\bjava/", "org/teavm/classlib/java/")
                        //             .replaceAll("(\\w+)\\z", "T$1")
                        // }

                        super.visit(version, access, name, signature, superName, interfaces)

                    else super.visit(version, access, implName, signature, superName, interfaces)

            override def visitMethod(access: Int, name: (String), descriptor: (String), signature: (String), exceptions: (Array[(String) ]) ): (MethodVisitor)
            =
                /* calls super method, with mangled `descriptor`. */
                locally :
                    super.visitMethod(
                        access,
                        name,
                        demangleMethodTypeDesc(descriptor), null,
                        exceptions.pipeNonNull : exceptions =>
                            for { exception <- exceptions } yield demangleClassName(exception)
                        ,
                        )

        }

    private[teavm]
    def getMethodVisitorTransformer
        ()
        : asm.MethodVisitor => asm.MethodVisitor
    =
      v0 =>
        new asm.MethodVisitor(asm.Opcodes.ASM9, v0 ) { thisMv =>

            import language.unsafeNulls

            // import asm.{*}

            // override def visit(version: Int, access: Int, implName: (String) , signature: (String) , superName: (String) , interfaces: (Array[(String) ]) ): Unit
            // =
            //     implName.replaceFirst("\\Aorg/teavm/classlib/", "")
            //     .pipe: name =>

            //         if !(name matches "java/lang/(Object|String|Double|Float|Long|Int|Short|Char|Byte|Boolean|Number)" ) && (name startsWith "java/") then

            //             // locally {
            //             //             name
            //             //             .replaceFirst("\\bjava/", "org/teavm/classlib/java/")
            //             //             .replaceAll("(\\w+)\\z", "T$1")
            //             // }

            //             super.visit(version, access, name, signature, superName, interfaces)

            //         else super.visit(version, access, implName, signature, superName, interfaces)

        }

    /**
     * demangles a method descriptor of an implementation method (e.g. `(Lorg/teavm/classlib/java/lang/String;)V` or `(Lorg/teavm/classlib/java/lang/TString;)V`) to the corresponding standard library method descriptor (e.g. `(Ljava/lang/String;)V`), if applicable, otherwise returns the input unchanged.
     */
    def demangleMethodTypeDesc(descriptor: String): String
    =
            import language.unsafeNulls

            // import asm.{*}

            locally :

                        asm.Type.getType(descriptor )
                        .getArgumentTypes()
                        .map: t =>
                            t.getDescriptor().pipe { d => d.take(1) ++ d.drop(1).pipe { demangleClassName(_) } }
                        .mkString("(", "", ")")
                        .++ { (asm.Type.getType(descriptor ).getReturnType().getDescriptor() ).pipe { d => d.take(1) ++ d.drop(1).pipe { demangleClassName(_) } } }
                        // reconstructs the method descriptor, but with mangled types

    /**
     * demangles a class name of an implementation class (e.g. `org.teavm.classlib.java.lang.String` or `org.teavm.classlib.java.lang.TString`) to the corresponding standard library class name (e.g. `java.lang.String`), if applicable, otherwise returns the input unchanged.
     */
    def demangleClassName(implName: String)
        : String
    =
      if implName startsWith "org/teavm/classlib/" then
        implName
        .replaceAll("\\bT(\\w+)\\z", "$1")
        .replaceFirst("org/teavm/classlib/", "")
      else implName

    /**
     * the opposite/inverse of what `demangleClassName` does.
     */
    def mangleClassName(name: String)
        : String
    =
                            name
                            .replaceFirst("(?<=L|\\b)java/", "org/teavm/classlib/java/")
                            .replaceAll("(\\w+)\\z", "T$1")

} // InmemStdLib






