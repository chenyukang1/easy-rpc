package com.cyk.easy.rpc.remoting.zookeeper;

import com.cyk.easy.rpc.common.Configuration;
import com.cyk.easy.rpc.common.RegistryConfig;
import com.cyk.easy.rpc.common.URL;
import com.cyk.easy.rpc.common.utils.ConcurrentHashSet;
import com.cyk.easy.rpc.common.utils.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Curator5ZookeeperClient is a Zookeeper client implementation using Apache Curator 5.
 * It provides methods to create, delete, and retrieve children nodes in Zookeeper.
 *
 * @author yukang.chen
 */
public class Curator5ZookeeperClient implements ZookeeperClient {

    private static final Logger logger = LoggerFactory.getLogger(Curator5ZookeeperClient.class);

    private final Set<String> persistentExistNodePath = new ConcurrentHashSet<>();

    private final CuratorFramework curator;

    private volatile boolean closed = false;

    public Curator5ZookeeperClient(URL url) {
        RegistryConfig registryConfig = Configuration.getInstance().getRegistryConfig();
        Integer timeout = registryConfig.getTimeout();
        try {
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                    .connectString(url.getPrimaryAndBackupConnString())
                    .retryPolicy(new RetryNTimes(1, 1000))
                    .connectionTimeoutMs(timeout)
                    .sessionTimeoutMs(registryConfig.getSessionTimeout());
            String userInformation = registryConfig.getUserInformation();
            if (StringUtils.isNotEmpty(userInformation)) {
                builder = builder.authorization("digest", userInformation.getBytes(StandardCharsets.UTF_8));
                builder.aclProvider(new ACLProvider() {
                    @Override
                    public List<ACL> getDefaultAcl() {
                        return ZooDefs.Ids.CREATOR_ALL_ACL;
                    }

                    @Override
                    public List<ACL> getAclForPath(String path) {
                        return ZooDefs.Ids.CREATOR_ALL_ACL;
                    }
                });
            }
            curator = builder.build();
            curator.start();
            boolean connected = curator.blockUntilConnected(timeout, TimeUnit.MILLISECONDS);
            if (!connected) {
                throw new RemotingException("Failed to connect to Zookeeper within the timeout period.");
            }
        } catch (InterruptedException e) {
            throw new RemotingException(e.getMessage(), e);
        }
    }

    @Override
    public void create(String path, boolean ephemeral) {
        if (!ephemeral) {
            if (persistentExistNodePath.contains(path)) {
                return; // Node already exists, no need to create it again
            }
            if (checkExists(path)) {
                persistentExistNodePath.add(path);
                return; // Node already exists, no need to create it again
            }
            try {
                curator.create().withMode(CreateMode.PERSISTENT).forPath(path);
                persistentExistNodePath.add(path);
            } catch (Exception e) {
                throw new RemotingException(e.getMessage(), e);
            }
        } else {
            try {
                curator.create().withMode(CreateMode.EPHEMERAL).forPath(path);
            } catch (Exception e) {
                throw new RemotingException(e.getMessage(), e);
            }
        }
    }

    @Override
    public void delete(String path) {
        try {
            curator.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            throw new RemotingException(e.getMessage(), e);
        }
        persistentExistNodePath.remove(path);
    }

    @Override
    public List<String> getChildren(String path) {
        try {
            return curator.getChildren().forPath(path);
        } catch (Exception e) {
            throw new RemotingException(e.getMessage(), e);
        }
    }

    @Override
    public void createOrUpdate(String path, String content, boolean ephemeral) {
        if (!checkExists(path)) {
            create(path, ephemeral);
        }
        try {
            curator.setData().forPath(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RemotingException(e.getMessage(), e);
        }
    }

    @Override
    public String getContent(String path) {
        if (!checkExists(path)) {
            return null;
        }
        byte[] bytes;
        try {
            bytes = curator.getData().forPath(path);
        } catch (Exception e) {
            throw new RemotingException(e.getMessage(), e);
        }
        if (bytes != null && bytes.length > 0) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return null;
    }

    @Override
    public boolean isConnected() {
        return curator.getZookeeperClient().isConnected();
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        curator.close();
        closed = true;
    }

    private boolean checkExists(String path) {
        try {
            return curator.checkExists().forPath(path) != null;
        } catch (Exception ex) {
            logger.error("Failed to check if path exists: {}", path, ex);
        }
        return false;
    }
}
