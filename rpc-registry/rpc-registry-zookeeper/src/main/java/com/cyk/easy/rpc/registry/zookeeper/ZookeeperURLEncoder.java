package com.cyk.easy.rpc.registry.zookeeper;

import com.cyk.easy.rpc.common.URL;
import com.cyk.easy.rpc.common.URLEncoder;

import static com.cyk.easy.rpc.common.constant.Constants.PATH_SEPARATOR;

public class ZookeeperURLEncoder implements URLEncoder {

    @Override
    public String toUrlPath(URL url) {
        return encode(url.service()) + PATH_SEPARATOR + encode(url.toString());
    }
}
