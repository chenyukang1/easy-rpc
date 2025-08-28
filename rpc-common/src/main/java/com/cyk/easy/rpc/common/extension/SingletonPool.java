package com.cyk.easy.rpc.common.extension;

/**
 * Singleton object pool
 *
 * @author yukang.chen
 */
public class SingletonPool {

    private static final ConcurrentLazyCache<String, Object> OBJECT_CACHE = new ConcurrentLazyCache<>();

    public static <T> T getInstance(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        return (T) OBJECT_CACHE.get(clazz.getName(), key -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new ExtensionException("Failed to create instance: " + clazz, e);
            }
        });
    }
}
