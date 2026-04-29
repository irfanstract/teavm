
package org.teavm

package vm.spi






object TvmJvmAstUtilLibSpecific :

    /**
     * in order for `org.teavm.model.util.BasicBlockSplitter` to resolve `RedundantJumpElimination` plugin, it needs to load the plugin classloader. This method is used for that purpose.
     */
    def getStaticPluginClassLoader()
        : ClassLoader
    =
        import language.unsafeNulls
        Class.forName("org.teavm.vm.TeaVM", false, Thread.currentThread().getContextClassLoader() ).getClassLoader()




