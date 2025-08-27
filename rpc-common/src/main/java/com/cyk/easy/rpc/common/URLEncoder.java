package com.cyk.easy.rpc.common;

import com.cyk.easy.rpc.common.utils.StringUtils;

import java.nio.charset.StandardCharsets;

import static com.cyk.easy.rpc.common.constant.Constants.PATH_SEPARATOR;
import static com.cyk.easy.rpc.common.constant.Constants.PROVIDERS_CATEGORY;

public interface URLEncoder {

    /**
     * To url path in service registry.
     *
     * @param url the url
     * @return the string
     */
    String toUrlPath(URL url);

    /**
     * To category path in service registry.
     *
     * @param url the url
     * @return the string
     */
    default String toCategoryPath(URL url) {
        return encode(url.service()) + PATH_SEPARATOR + PROVIDERS_CATEGORY;
    }

    default String encode(String value) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
