package com.cyk.easy.rpc.registry;

import com.cyk.easy.rpc.common.URL;

import java.util.List;

/**
 * The interface Registry service.
 */
public interface RegistryService {

    /**
     * Register.
     */
    void register(URL url);

    /**
     * Unregister.
     *
     * @param url the url
     */
    void unregister(URL url);

    /**
     * Lookup list.
     *
     * @param url the url
     * @return the list
     */
    List<URL> lookup(URL url);

}
