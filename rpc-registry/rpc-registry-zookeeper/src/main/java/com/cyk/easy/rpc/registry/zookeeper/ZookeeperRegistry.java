package com.cyk.easy.rpc.registry.zookeeper;

import com.cyk.easy.rpc.common.URL;
import com.cyk.easy.rpc.registry.AbstractRegistry;

import java.util.Collections;
import java.util.List;

public class ZookeeperRegistry extends AbstractRegistry {

    @Override
    public void doRegister(URL url) {

    }

    @Override
    public void doUnregister(URL url) {

    }

    @Override
    public List<URL> lookup(URL url) {
        return Collections.emptyList();
    }
}
