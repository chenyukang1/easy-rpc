package com.cyk.easy.rpc.common;

import com.cyk.easy.rpc.common.utils.StringUtils;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.cyk.easy.rpc.common.constant.Constants.*;


public interface URLDecoder {

    /**
     * From url path in service registry to url.
     *
     * @param path the path
     * @return the url
     */
    default URL decode(String encodedPath) {
        if (StringUtils.isEmpty(encodedPath)) {
            return null;
        }
        String rawUrlPath = java.net.URLDecoder.decode(encodedPath, StandardCharsets.UTF_8);
        int protocolIdx = rawUrlPath.indexOf(PROTOCOL_SEPARATOR);
        if (protocolIdx == -1) {
            throw new URLParseException("Invalid url path: " + rawUrlPath);
        }

        String protocol = rawUrlPath.substring(0, protocolIdx);
        String username = null;
        String password = null;
        String rest = rawUrlPath.substring(protocolIdx + PROTOCOL_SEPARATOR.length());
        int userInfoIdx = rest.indexOf(USERINFO_SEPARATOR);
        if (userInfoIdx > 0) {
            username = rest.substring(0, userInfoIdx);
            String userInfoStr = rest.substring(0, userInfoIdx);
            int passwordIdx = userInfoStr.indexOf(GROUP_CHAR_SEPARATOR);
            if (passwordIdx > 0) {
                password = userInfoStr.substring(passwordIdx + 1);
            }
            rest = rest.substring(userInfoIdx + USERINFO_SEPARATOR.length());
        }

        int pathIdx = rest.indexOf(PATH_SEPARATOR);
        if (pathIdx == -1) {
            throw new URLParseException("Invalid url path: " + rawUrlPath);
        }

        InetSocketAddress address = null;
        List< InetSocketAddress > backup = new ArrayList<>();
        String[] addressStrs = rest.substring(0, pathIdx).split(ADDRESS_SEPARATOR);
        for (int i = 0; i < addressStrs.length; i++) {
            if (StringUtils.isEmpty(addressStrs[i])) {
                throw new URLParseException("Invalid url path: " + rawUrlPath);
            }
            int portIdx = addressStrs[i].indexOf(GROUP_CHAR_SEPARATOR);
            if (portIdx == -1) {
                throw new URLParseException("Invalid url path: " + rawUrlPath);
            }
            String host = addressStrs[i].substring(0, portIdx);
            int port = Integer.parseInt(addressStrs[i].substring(portIdx + 1));
            if (i == 0) {
                address = new InetSocketAddress(host, port);
            } else {
                backup.add(new InetSocketAddress(host, port));
            }
        }
        String service = rest.substring(pathIdx + PATH_SEPARATOR.length());

        return new URL(protocol, address, backup, username, password, service);
    }
}
