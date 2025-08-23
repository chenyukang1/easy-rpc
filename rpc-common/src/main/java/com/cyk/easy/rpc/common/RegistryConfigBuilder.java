package com.cyk.easy.rpc.common;

public class RegistryConfigBuilder {

    private String address;

    private String[] backups;

    private String username;

    private String password;

    private Integer port;

    private String protocol;

    private Integer timeout;

    private Integer sessionTimeout;


    public static RegistryConfigBuilder builder() {
        return new RegistryConfigBuilder();
    }

    public RegistryConfigBuilder address(String address) {
        this.address = address;
        return this;
    }

    public RegistryConfigBuilder backups(String... backups) {
        this.backups = backups;
        return this;
    }

    public RegistryConfigBuilder username(String username) {
        this.username = username;
        return this;
    }

    public RegistryConfigBuilder password(String password) {
        this.password = password;
        return this;
    }

    public RegistryConfigBuilder port(Integer port) {
        this.port = port;
        return this;
    }

    public RegistryConfigBuilder protocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public RegistryConfigBuilder timeout(Integer timeout) {
        this.timeout = timeout;
        return this;
    }

    public RegistryConfigBuilder sessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
        return this;
    }

    public RegistryConfig build() {
        RegistryConfig registryConfig = new RegistryConfig();

        registryConfig.setAddress(this.address);
        registryConfig.setBackups(this.backups);
        registryConfig.setUsername(this.username);
        registryConfig.setPassword(this.password);
        registryConfig.setPort(this.port);
        registryConfig.setProtocol(this.protocol);
        registryConfig.setTimeout(this.timeout);
        registryConfig.setSessionTimeout(this.sessionTimeout);

        return registryConfig;
    }
}
