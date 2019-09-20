package liusheng.main.plugin;


import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Stream;

public class PluginLoader {
    private ServiceLoader<Plugin> serviceLoader;

    public List<Plugin> plugins() {
        serviceLoader = ServiceLoader.load(Plugin.class, getClassLoader());
        List<Plugin> list = new LinkedList<>();
        for (Plugin plugin : serviceLoader) {

            list.add(plugin);
        }

        return list;
    }

    private ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = PluginLoader.class.getClassLoader();
            if (null == classLoader) {
                classLoader = ClassLoader.getSystemClassLoader();
            }
        }
        return classLoader;
    }
}
