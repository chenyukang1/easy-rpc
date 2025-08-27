package com.cyk.easy.rpc.common;

import com.cyk.easy.rpc.common.utils.Assert;
import com.cyk.easy.rpc.common.utils.StringUtils;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cyk.easy.rpc.common.constant.Constants.*;

/**
 * protocol://[username[:password]@]host:port/service
 *
 * @param protocol 服务协议
 * @param address  服务地址
 * @param backup   备份地址
 * @param username 用户名
 * @param password 密码
 * @param service  服务名
 * @author yukang.chen
 */
public record URL(String protocol, InetSocketAddress address, List<InetSocketAddress> backup, String username,
                  String password, String service) implements Serializable {

    public URL {
        Assert.notEmpty(protocol, "Protocol can not be empty");
        Assert.notNull(address, "Address can not be null");
        Assert.notNull(service, "Service can not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        URL url = (URL) o;
        return Objects.equals(protocol, url.protocol) && Objects.equals(address, url.address) && Objects.equals(service, url.service);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(protocol() + PROTOCOL_SEPARATOR);
        if (StringUtils.isNotEmpty(username())) {
            builder.append(username());
            if (StringUtils.isNotEmpty(password())) {
                builder.append(GROUP_CHAR_SEPARATOR).append(password());
            }
            builder.append(USERINFO_SEPARATOR);
        }
        builder.append(address().getHostName()).append(GROUP_CHAR_SEPARATOR).append(address().getPort());
        if (backup() != null && !backup().isEmpty()) {
            for (InetSocketAddress backupAddress : backup()) {
                builder.append(ADDRESS_SEPARATOR).append(backupAddress.getHostName())
                        .append(GROUP_CHAR_SEPARATOR).append(backupAddress.getPort());
            }
        }
        return builder.toString();
    }

    public List<String> getPrimaryAndBackupAddress() {
        List<String> addressList = new ArrayList<>();
        addressList.add(address().getHostName() + GROUP_CHAR_SEPARATOR + address().getPort());
        if (backup() != null && !backup().isEmpty()) {
            for (InetSocketAddress backupAddress : backup()) {
                addressList.add(backupAddress.getHostName() + GROUP_CHAR_SEPARATOR + backupAddress.getPort());
            }
        }
        return addressList;
    }

    /**
     * Splice the primary and backup addresses into a single string.
     *
     * @return the primary and backup address
     */
    public String getPrimaryAndBackupConnString() {
        StringBuilder builder =
                new StringBuilder(protocol() + PROTOCOL_SEPARATOR + address().getHostName() + GROUP_CHAR_SEPARATOR + address().getPort());
        if (backup() != null && !backup().isEmpty()) {
            for (InetSocketAddress backupAddress : backup()) {
                builder.append(ADDRESS_SEPARATOR).append(backupAddress.getHostName()).append(GROUP_CHAR_SEPARATOR).append(backupAddress.getPort());
            }
        }
        return builder.toString();
    }

    public static URLBuilder builder() {
        return new URLBuilder();
    }

    public static class URLBuilder {

        private String protocol;

        private InetSocketAddress address;

        private List<InetSocketAddress> backup;

        private String username;

        private String password;

        private String service;

        private URLBuilder() {
        }

        public URLBuilder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public URLBuilder address(InetSocketAddress address) {
            this.address = address;
            return this;
        }

        public URLBuilder backup(List<InetSocketAddress> backup) {
            this.backup = backup;
            return this;
        }

        public URLBuilder username(String username) {
            this.username = username;
            return this;
        }

        public URLBuilder password(String password) {
            this.password = password;
            return this;
        }

        public URLBuilder service(String service) {
            this.service = service;
            return this;
        }

        public URL build() {
            return new URL(protocol, address, backup, username, password, service);
        }
    }
}
