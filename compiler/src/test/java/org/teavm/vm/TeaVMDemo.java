
package org.teavm.vm;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

import org.teavm.backend.javascript.JavaScriptTarget;
import org.teavm.model.ClassHolderSource;
import org.teavm.parsing.ClasspathClassHolderSource;

public class TeaVMDemo {

    public static void main(String... args) {

        ClassLoader classLoader = (
          // ClassLoader.getSystemClassLoader()
          Thread.currentThread().getContextClassLoader()
        ) ; // obtain ClassLoader somewhere
        ClassHolderSource classSource = new ClasspathClassHolderSource(new org.teavm.model.ReferenceCache() );
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
        vm.setEntryPoint("org.teavm.vm.TeaVMDemo");
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
            System.err.println(String.format("File %s: %s", "index.js", new String(bt.getContent("index.js"), StandardCharsets.UTF_8 ) ) );
        }

    }

    // .

}

