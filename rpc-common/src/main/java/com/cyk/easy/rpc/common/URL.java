package com.cyk.easy.rpc.common;

import com.cyk.easy.rpc.common.utils.Assert;
import com.cyk.easy.rpc.common.utils.StringUtils;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.cyk.easy.rpc.common.constant.Constants.PATH_SEPARATOR;
import static com.cyk.easy.rpc.common.constant.Constants.PROVIDERS_CATEGORY;

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
        StringBuilder builder = new StringBuilder(protocol() + "://");
        if (StringUtils.isNotEmpty(username())) {
            builder.append(username());
            if (StringUtils.isNotEmpty(password())) {
                builder.append(":").append(password());
            }
            builder.append("@");
        }
        builder.append(address().getHostName()).append(":").append(address().getPort());
        if (backup() != null && !backup().isEmpty()) {
            for (InetSocketAddress backupAddress : backup()) {
                builder.append(",").append(backupAddress.getHostName()).append(":").append(backupAddress.getPort());
            }
        }
        return builder.toString();
    }

    public List<String> getPrimaryAndBackupAddress() {
        List<String> addressList = new ArrayList<>();
        addressList.add(address().getHostName() + ":" + address().getPort());
        if (backup() != null && !backup().isEmpty()) {
            for (InetSocketAddress backupAddress : backup()) {
                addressList.add(backupAddress.getHostName() + ":" + backupAddress.getPort());
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
                new StringBuilder(protocol() + "://" + address().getHostName() + ":" + address().getPort());
        if (backup() != null && !backup().isEmpty()) {
            for (InetSocketAddress backupAddress : backup()) {
                builder.append(",").append(backupAddress.getHostName()).append(":").append(backupAddress.getPort());
            }
        }
        return builder.toString();
    }

    public String toURLPath() {
        return URL.encode(this.service()) + PATH_SEPARATOR + URL.encode(this.toString());
    }

    public String toCategoryPath() {
        String servicePath = URL.encode(this.service());
        return servicePath + PATH_SEPARATOR + PROVIDERS_CATEGORY;
    }

    public static String encode(String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
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
