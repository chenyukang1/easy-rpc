package com.cyk.easy.rpc.common;

public enum Protocol {

    HTTP("http"),

    HTTP2("http2");

    private final String name;

    Protocol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
