package com.cyk.easy.rpc.common;

import com.cyk.easy.rpc.common.utils.StringUtils;

import java.nio.charset.StandardCharsets;

public interface URLEncoder {

    String toUrlPath(URL url);

    static String encode(String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
