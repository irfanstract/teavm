
package org.teavm
package vm

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.Locale

import org.objectweb.{asm}
import asm.tree.{ClassNode}
import org.teavm.parsing.AsmTxUtil
import org.teavm.backend.javascript.JavaScriptTarget
import org.teavm.model.{ClassHolder, ClassHolderSource}
import org.teavm.parsing.{ClasspathClassHolderSource, ClasspathResourceProvider}
import org.teavm.parsing.{TransformedResourceProvider1}
import org.teavm.classlib.impl.{InmemStdLib}
import org.teavm.parsing.resource.{Resource}










object TeaVMDemo2 :

    def main(args: String*): Unit
    =
        ???



object TeaVMDemoImpl :

    locally { }

    def demangleClassName(implName: String): String
    = InmemStdLib.demangleClassName(implName)

    def mangleClassName(name: String)
        : String
    =
        (
                        if !(name matches "java\\.lang\\.(Object|String|Double|Float|Long|Int|Short|Char|Byte|Boolean|Number)" ) && (name startsWith "java." ) then
                        (
                        ((
                            name
                            .replaceFirst("\\bjava\\.", "org.teavm.classlib.java.")
                            .replaceAll("(\\w+)\\z", "T$1")
                        ) )
                        )
                        else
                        name
        )

    /**
     * parse the classfile, return the ClassNode.
     */
    def parseClassFile(cls: Resource )
        : ClassNode
    =
        AsmTxUtil.Prep.parseClassFile(cls)

    locally { }

    def getAsClassHolderSource(classLoader: ClassLoader)
    =
        /* avoiding the explicable "`null` is not comparable" complaints on `null`-check(s) */
        import language.unsafeNulls

        // ClassHolderSource classSource = new ClasspathClassHolderSource((
        //     TransformedResourceProvider1.getSingleProcInstance(new ClasspathResourceProvider(classLoader ) { }, (name, r) -> {
        //           if (true) {
        //             if ((!name.matches("java/lang/(Object|String|Double|Float|Long|Int|Short|Char|Byte|Boolean|Number)\\.class") ) && TeaVMDemoImpl.demangleClassName(name).startsWith("java/") ) {
        //                 if (r != null) {
        //                     var r2 = InmemStdLib.transformClassfile(r) ;
        //                     if ((
        //                         name.matches(".*java/lang/T?(Thread|Interrup).*")
        //                     ) ) {
        //                         System.err.println(String.format("redirecting %s to %s", name, TeaVMDemoImpl.parseClassFile(r2).name ) );
        //                     }
        //                     return r2 ;
        //                 }
        //             }
        //           }
        //           return r;
        //     } )
        // ), new org.teavm.model.ReferenceCache() ) {
        //     @Override
        //     public ClassHolder get(String name) {
        //         if (name.endsWith("jdk.internal.misc.Unsafe")) {
        //             System.err.println(String.format("refusing to resolve 'Unsafe': %s", name ) );
        //             return null ;
        //         }
        //         {
        //             var nm2 = (
        //                 TeaVMDemoImpl.mangleClassName(name)
        //             );
        //             if (nm2 != null && !(nm2.equals(name) ) ) {
        //                 //
        //                 var r2 = super.get(nm2) ;
        //                 if (r2 != null ) {
        //                     System.err.println(String.format("redirected %s to %s -- %s", name, nm2, r2.getName() ) );
        //                     return r2;
        //                 }
        //             }
        //             return super.get(name) ;
        //         }
        //     }
        // };
        val classSource = new ClasspathClassHolderSource(
            TransformedResourceProvider1.getSingleProcInstance(new ClasspathResourceProvider(classLoader ) { } )( (name, r) => util.boundary { retu ?=>
            new Function0[Resource ] {
            override def apply(): Resource =
                if true then {
                    if !(name matches "java/lang/(Object|String|Double|Float|Long|Int|Short|Char|Byte|Boolean|Number)\\.class" ) && (name.pipe { TeaVMDemoImpl.demangleClassName(_) } startsWith "java/" ) then
                        if (r != null) then
                            val r2 = InmemStdLib.transformClassfile(r)
                            if ((
                                name.matches(".*java/lang/T?(Thread|Interrup).*")
                            ) ) then
                                System.err.println { s"redirecting $name to ${TeaVMDemoImpl.parseClassFile(r2).name}" }
                            util.boundary.break(r2)
                }
                util.boundary.break(r)
            }.apply()
            } ), new org.teavm.model.ReferenceCache() ) {

            override def get(name: String): ClassHolder =
                if (
                        (name endsWith "jdk.internal.misc.Unsafe" )
                    || (name contains "SharedSecrets" )
                    || (name.toUpperCase(Locale.ROOT) contains "WIN" )
                    || (name.toUpperCase(Locale.ROOT) contains "OSX" )
                    || ((name.toUpperCase(Locale.ROOT) contains "LAMBDA$" ) && !((name startsWith "java." ) || (name startsWith "scala." ) ) )
                    // || (name contains "jdk.internal" )
                    || (name contains "jdk.internal.loader." )
                    || (name contains "jdk.internal.foreign." )
                    || ((name contains "sun.util." ) && !(name contains ".locale." ) )
                    || (name contains "sun.security" )
                    || (name matches "java\\.util\\.concurrent\\.(?=[a-z])" )
                    || (name matches "scala\\.collection\\.mutable\\.(?=[a-z])" )
                ) then {
                    System.err.println { s"refusing to resolve: $name" }
                    null
                } else {
                    val nm2 = TeaVMDemoImpl.mangleClassName(name)
                    if (nm2 != null && !(nm2 == name ) ) then {
                        //
                        val r2 = super.get(nm2)
                        if (r2 != null ) then {
                            System.err.println { s"redirected $name to $nm2 -- ${r2.getName()}" }
                            r2
                        } else super.get(name)
                    } else super.get(name)
                }

        }
        classSource









