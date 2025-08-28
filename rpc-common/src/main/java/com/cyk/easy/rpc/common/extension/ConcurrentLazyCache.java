package com.cyk.easy.rpc.common.extension;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Concurrent lazy initialization cache
 *
 * @author yukang.chen
 */
public class ConcurrentLazyCache<K, V> {

    private final ConcurrentMap<K, Holder<V>> cache = new ConcurrentHashMap<>();

    public V get(K key, Function<K, V> mappingFunction) {
        if (key == null) {
            throw new ExtensionException("Key can not be null");
        }
        Holder<V> holder = cache.get(key);
        if (holder == null) {
            cache.putIfAbsent(key, new Holder<>());
            holder = cache.get(key);
        }
        V value = holder.get();
        if (value == null) {
            synchronized (holder) {
                value = holder.get();
                if (value == null) {
                    value = mappingFunction.apply(key);
                    holder.set(value);
                }
            }
        }
        return value;
    }
}
