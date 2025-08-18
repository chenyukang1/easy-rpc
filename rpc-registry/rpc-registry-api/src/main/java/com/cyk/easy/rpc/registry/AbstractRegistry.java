package com.cyk.easy.rpc.registry;

import com.cyk.easy.rpc.common.ConcurrentHashSet;
import com.cyk.easy.rpc.common.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public abstract class AbstractRegistry implements RegistryService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractRegistry.class);

    private final Set<URL> registered = new ConcurrentHashSet<>();

    @Override
    public void register(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url can not be null");
        }
        if (url.address().getPort() != 0) {
            if (logger.isInfoEnabled()) {
                logger.info("Register: {}", url);
            }
        }
        registered.add(url);
        doRegister(url);
    }

    @Override
    public void unregister(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("url can not be null");
        }
        if (url.address().getPort() != 0) {
            if (logger.isInfoEnabled()) {
                logger.info("Unregister: {}", url);
            }
        }
        registered.remove(url);
        doUnregister(url);
    }

    public abstract void doRegister(URL url);

    public abstract void doUnregister(URL url);
}
