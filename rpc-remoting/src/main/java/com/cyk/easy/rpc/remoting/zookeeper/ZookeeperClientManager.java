package com.cyk.easy.rpc.remoting.zookeeper;

import com.cyk.easy.rpc.common.Configuration;
import com.cyk.easy.rpc.common.URL;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZookeeperClientManager {

    private static volatile ZookeeperClientManager instance;

    private final Map<String, ZookeeperClient> zookeeperClientMap = new ConcurrentHashMap<>();

    private ZookeeperClientManager() {
        // Private constructor to prevent instantiation
        connect(Configuration.getInstance().getRegistryConfig().toURL()); // Initialize the connection
    }

    /**
     * Get the singleton instance of ZookeeperClientManager.
     *
     * @return the singleton instance
     */
    public static ZookeeperClientManager getInstance() {
        if (instance == null) {
            synchronized (ZookeeperClientManager.class) {
                if (instance == null) {
                    instance = new ZookeeperClientManager();
                }
            }
        }
        return instance;
    }

    public ZookeeperClient connect(URL url) {
        List<String> addressList = url.getPrimaryAndBackupAddress();
        ZookeeperClient zookeeperClient;
        if ((zookeeperClient = fetchZookeeperClient(addressList)) != null && zookeeperClient.isConnected()) {
            for (String address : addressList) {
                zookeeperClientMap.put(address, zookeeperClient);
            }
            return zookeeperClient; // Already connected
        }
        synchronized (zookeeperClientMap) {
            if ((zookeeperClient = fetchZookeeperClient(addressList)) != null && zookeeperClient.isConnected()) {
                for (String address : addressList) {
                    zookeeperClientMap.put(address, zookeeperClient);
                }
                return zookeeperClient; // Already connected
            }
            zookeeperClient = new Curator5ZookeeperClient(url);
            for (String address : addressList) {
                zookeeperClientMap.put(address, zookeeperClient);
            }
        }
        return zookeeperClient; // New connection
    }

    private ZookeeperClient fetchZookeeperClient(List<String> addressList) {
        for (String address : addressList) {
            ZookeeperClient zookeeperClient = zookeeperClientMap.get(address);
            if (zookeeperClient != null && zookeeperClient.isConnected()) {
                return zookeeperClient; // Return the first connected client
            }
        }
        return null; // No connected client found
    }
}
