package com.cyk.easy.rpc.remoting.zookeeper;

import java.util.List;

public interface ZookeeperClient {

    void create(String path, boolean ephemeral);

    void delete(String path);

    List<String> getChildren(String path);

    void createOrUpdate(String path, String content, boolean ephemeral);

    String getContent(String path);

    boolean isConnected();

    void close();
}
