package org.teavm.vm;

import org.teavm.vm.spi.TeaVMPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public final class TeaVMPluginLoader {
    private TeaVMPluginLoader() { }

    public static Iterable<TeaVMPlugin> load(ClassLoader classLoader) {
        List<TeaVMPlugin> result = new ArrayList<>();
        ServiceLoader<TeaVMPlugin> loader = ServiceLoader.load(TeaVMPlugin.class, classLoader);
        for (TeaVMPlugin plugin : loader) {
            result.add(plugin);
        }
        return result;
    }
}
