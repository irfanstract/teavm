
package org.teavm.vm;

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
import org.teavm.classlib.impl.InmemStdLib;

public class TeaVMDemo {

    public static void main(String... args) {

        ClassLoader classLoader = (
            // ClassLoader.getSystemClassLoader()
            //   Thread.currentThread().getContextClassLoader()
            // TeaVMDemo.class.getClassLoader()
            org.teavm.tooldemos.HelloWorldCli.class.getClassLoader()
        ) ; // obtain ClassLoader somewhere
        ClassHolderSource classSource = TeaVMDemoImpl.getAsClassHolderSource(classLoader) ;
        TeaVM vm = new TeaVMBuilder(new JavaScriptTarget() )
                .setClassLoader(classLoader)
                .setClassSource(classSource)
                .build();
        // vm.setMinifying(false); // optionally disable obfuscation
        vm.installPlugins();    // install all default plugins
                                // that are found in a classpath
        vm.setProgressListener((
            new TeaVMProgressListener() {

							@Override
							public TeaVMProgressFeedback phaseStarted(TeaVMPhase phase, int count) {
								// // TODO Auto-generated method stub
								// throw new UnsupportedOperationException("Unimplemented method 'phaseStarted'");
                                System.err.println(MessageFormat.format("{0} at {1}", phase, count) );
                                return TeaVMProgressFeedback.CONTINUE;
							}

							@Override
							public TeaVMProgressFeedback progressReached(int progress) {
								// // TODO Auto-generated method stub
								// throw new UnsupportedOperationException("Unimplemented method 'progressReached'");
                                return TeaVMProgressFeedback.CONTINUE;
							}
            }
        ));;
        vm.setEntryPoint("org.teavm.tooldemos.HelloWorldCli");
        var bt = new MemoryBuildTarget();
        vm.build(bt, "index.js");
        // vm.checkForMissingItems();
        for (var problem : vm.getProblemProvider().getProblems() ) {
          System.err.println(String.format("problem: %s: %s", problem.getSeverity(), problem.getText()) ) ;
        }

        System.err.println((IntSupplier) () -> 5 );
        System.err.println((Consumer<Object>) e -> {} );

        System.err.println("finished");
        for (var nm : bt.getNames() ) {
            System.err.println(String.format("File %s: %dB", nm, bt.getContent(nm).length));
        }
        if (bt.getNames().contains("index.js") ) {
            var c = new String(bt.getContent("index.js"), StandardCharsets.UTF_8 );
            if (c.length() <= 20000 ) {
                System.err.println(String.format("File %s: %s", "index.js", c ) );
            } else {
                System.err.println(String.format("File %s: %s", "index.js", c.substring(0, 38000) ) );
            }
        }

    }

    // .

}

