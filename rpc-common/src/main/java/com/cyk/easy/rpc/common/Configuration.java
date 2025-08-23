package com.cyk.easy.rpc.common;

public class Configuration {

    private static final Configuration INSTANCE = new Configuration();

    private RegistryConfig registryConfig;

    private Configuration() {}

    public static Configuration getInstance() {
        return INSTANCE;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }
}
