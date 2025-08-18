package com.cyk.easy.rpc.common;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * The class URL
 *
 * @param protocol 服务协议
 * @param address  服务地址
 * @param service  服务名
 * @author yukang.chen
 * @date 2025/7/22
 */
public record URL(Protocol protocol, InetSocketAddress address, String service) implements Serializable {

    public URL {
        checkParam(protocol, address, service);
    }

    private void checkParam(Protocol protocol, InetSocketAddress address, String service) {
        if (protocol == null) {
            throw new IllegalArgumentException("protocol can not be null");
        }
        if (address == null) {
            throw new IllegalArgumentException("address can not be null");
        }
        if (StringUtils.isEmpty(service)) {
            throw new IllegalArgumentException("service can not be empty");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        URL url = (URL) o;
        return protocol == url.protocol && Objects.equals(address, url.address) && Objects.equals(service, url.service);
    }

    @Override
    public String toString() {
        return protocol.getName() + "://" + address.getHostName() + ":" + address.getPort() + "/" + service;
    }
}
