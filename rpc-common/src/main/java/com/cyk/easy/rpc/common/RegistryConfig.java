package com.cyk.easy.rpc.common;

import com.cyk.easy.rpc.common.constant.RegistryConstants;
import com.cyk.easy.rpc.common.utils.StringUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for the registry center.
 * This class holds the configuration details required to connect to a registry center.
 * It includes fields for address, username, password, port, protocol, and timeout.
 */
public class RegistryConfig {

    /**
     * Register center address.
     */
    private String address;

    /**
     * Backup address for the register center.
     */
    private String[] backups;

    /**
     * Username to login the register center.
     */
    private String username;

    /**
     * Password to login the register center.
     */
    private String password;

    /**
     * Default port for the register center.
     */
    private Integer port;

    /**
     * Protocol used for the register center.
     */
    private String protocol;

    /**
     * Connect timeout in milliseconds for the register center.
     */
    private Integer timeout;

    /**
     * Session timeout in milliseconds for the register center.
     */
    private Integer sessionTimeout;

    RegistryConfig() {}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String[] getBackups() {
        return backups;
    }

    public void setBackups(String[] backups) {
        this.backups = backups;
    }

    public String getUserInformation() {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(getUsername()) && StringUtils.isEmpty(getPassword())) {
            return null;
        }
        if (StringUtils.isNotEmpty(getUsername())) {
            builder.append(getUsername());
        }
        builder.append(":");
        if (StringUtils.isNotEmpty(getPassword())) {
            builder.append(getPassword());
        }

        return builder.length() == 1 ? null : builder.toString();
    }

    /**
     * Splice the primary and backup addresses into a single string.
     *
     * @return the primary and backup address
     */
    public URL toURL() {
        InetSocketAddress address = new InetSocketAddress(getAddress(), getPort());
        List<InetSocketAddress> backup = new ArrayList<>();
        if (getBackups() != null) {
            for (String backupAddress : getBackups()) {
                if (StringUtils.isEmpty(backupAddress)) {
                    continue;
                }
                String[] parts = backupAddress.split(":");
                if (parts.length != 2) {
                    throw new IllegalArgumentException("Invalid backup address format: " + backupAddress);
                }
                backup.add(new InetSocketAddress(parts[0], Integer.parseInt(parts[1])));
            }
        }

        return URL.builder()
                .protocol(getProtocol())
                .address(address)
                .backup(backup)
                .username(getUsername())
                .password(getPassword())
                .service(RegistryConstants.REGISTRY_SERVICE)
                .build();
    }
}
