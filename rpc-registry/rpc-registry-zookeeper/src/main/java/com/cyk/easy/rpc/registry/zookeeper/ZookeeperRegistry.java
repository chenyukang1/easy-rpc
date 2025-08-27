package com.cyk.easy.rpc.registry.zookeeper;

import com.cyk.easy.rpc.common.URL;
import com.cyk.easy.rpc.common.URLDecoder;
import com.cyk.easy.rpc.common.URLEncoder;
import com.cyk.easy.rpc.registry.AbstractRegistry;
import com.cyk.easy.rpc.remoting.zookeeper.ZookeeperClient;
import com.cyk.easy.rpc.remoting.zookeeper.ZookeeperClientManager;

import java.util.List;

public class ZookeeperRegistry extends AbstractRegistry {

    private final ZookeeperClient zookeeperClient;

    private final URLEncoder urlEncoder = new ZookeeperURLEncoder();

    private final URLDecoder urlDecoder = new ZookeeperURLDecoder();

    public ZookeeperRegistry(URL url) {
        super(url);
        this.zookeeperClient = ZookeeperClientManager.getInstance().connect(url);
    }

    @Override
    public void doRegister(URL url) {
        checkDestroyed();
        zookeeperClient.create(urlEncoder.toUrlPath(url), true);
    }

    @Override
    public void doUnregister(URL url) {
        checkDestroyed();
        zookeeperClient.delete(urlEncoder.toUrlPath(url));
    }

    @Override
    public List<URL> lookup(URL url) {
        checkDestroyed();
        return zookeeperClient.getChildren(urlEncoder.toCategoryPath(url))
                .stream()
                .map(path -> urlDecoder.decode(path + urlEncoder.toCategoryPath(url)))
                .toList();
    }

    private void checkDestroyed() {
        if (zookeeperClient == null) {
            throw new IllegalStateException("registry is destroyed");
        }
    }
}
