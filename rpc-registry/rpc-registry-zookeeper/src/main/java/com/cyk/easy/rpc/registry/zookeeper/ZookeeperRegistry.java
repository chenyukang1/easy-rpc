package com.cyk.easy.rpc.registry.zookeeper;

import com.cyk.easy.rpc.common.URL;
import com.cyk.easy.rpc.registry.AbstractRegistry;
import com.cyk.easy.rpc.remoting.zookeeper.ZookeeperClient;
import com.cyk.easy.rpc.remoting.zookeeper.ZookeeperClientManager;

import java.util.Collections;
import java.util.List;

public class ZookeeperRegistry extends AbstractRegistry {

    private final ZookeeperClient zookeeperClient;

    protected ZookeeperRegistry(URL url) {
        super(url);

        this.zookeeperClient = ZookeeperClientManager.getInstance().connect(url);
    }

    @Override
    public void doRegister(URL url) {
        checkDestroyed();
        zookeeperClient.create(url.service(), true);
    }

    @Override
    public void doUnregister(URL url) {

    }

    @Override
    public List<URL> lookup(URL url) {
        checkDestroyed();
        zookeeperClient.getChildren(url.toCategoryPath());
        return Collections.emptyList();
    }

    private void checkDestroyed() {
        if (zookeeperClient == null) {
            throw new IllegalStateException("registry is destroyed");
        }
    }
}
