package com.cyk.easy.rpc.common.extension;

import com.cyk.easy.rpc.common.utils.Assert;
import com.cyk.easy.rpc.common.utils.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Extension loader for loading service provider implementations.
 * </br>
 * <a href="https://cn.dubbo.apache.org/zh-cn/overview/mannual/java-sdk/reference-manual/architecture/dubbo-spi/">dubbo-spi</a>
 */
public final class ExtensionLoader<T> {

    private static final String SERVICE_DIRECTORY = "META-INF/extensions/";

    private static final ConcurrentLazyCache<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentLazyCache<>();

    private final ConcurrentLazyCache<String, Object> instanceCache = new ConcurrentLazyCache<>();

    /**
     * Class cache, the key is the name read from the file, and the value is the corresponding class.
     */
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private final Class<?> type;

    private ExtensionLoader(Class<?> type) {
        this.type = type;
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Extension type should not be null.");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an interface!");
        }
        if (type.getAnnotation(SPI.class) == null) {
            throw new IllegalArgumentException("Extension type (" + type +
                    ") is not an extension, because it is NOT annotated with @" + SPI.class.getSimpleName() + "!");
        }
        return (ExtensionLoader<T>) EXTENSION_LOADERS.get(type, ExtensionLoader::new);
    }

    public T getExtension(String name) {
        Assert.notEmpty(name, "Extension name == null");
        return (T) instanceCache.get(name, this::createExtension);
    }

    private Object createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw new IllegalArgumentException("No such extension of name " + name);
        }
        return SingletonPool.getInstance(clazz);
    }

    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classMap = cachedClasses.get();
        if (classMap == null) {
            synchronized (cachedClasses) {
                classMap = cachedClasses.get();
                if (classMap == null) {
                    classMap = loadExtensionClasses();
                    cachedClasses.set(classMap);
                }
            }
        }
        return classMap;
    }

    private Map<String, Class<?>> loadExtensionClasses() {
        Map<String, Class<?>> extensionClasses = new HashMap<>();
        String path = SERVICE_DIRECTORY + type.getName();
        try {
            ClassLoader classLoader = ExtensionLoader.class.getClassLoader();
            Enumeration<URL> urls = classLoader.getResources(path);
            String clazzName;
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // 注释信息
                        final int markIdx = line.indexOf('#');
                        if (markIdx > -1) {
                            line = line.substring(0, markIdx);
                        }
                        line = line.trim();
                        if (!line.isEmpty()) {
                            int eqIdx = line.indexOf("=");
                            String name = null;
                            if (eqIdx > 0) {
                                name = line.substring(0, eqIdx).trim();
                                clazzName = line.substring(eqIdx + 1).trim();
                            } else {
                                clazzName = line.trim();
                            }
                            if (StringUtils.isNotEmpty(clazzName)) {
                                Class<?> clazz = classLoader.loadClass(clazzName);
                                if (StringUtils.isEmpty(name)) {
                                    Extension extension = clazz.getAnnotation(Extension.class);
                                    if (extension != null) {
                                        name = extension.value();
                                    } else {
                                        name = clazz.getSimpleName();
                                        if (name.endsWith(type.getSimpleName())) {
                                            name = name.substring(0, name.length() - type.getSimpleName().length());
                                        }
                                        name = name.toLowerCase();
                                    }
                                }
                                if (StringUtils.isEmpty(name)) {
                                    throw new ExtensionException("No such extension name " + name + " for class " +
                                            clazz.getCanonicalName());
                                }
                                extensionClasses.put(name, clazz);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new ExtensionException(e.getMessage(), e);
        }
        return extensionClasses;
    }

}
