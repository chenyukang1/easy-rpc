package com.cyk.easy.rpc.common.extension;

public class SPI1Impl implements SPI1 {
    @Override
    public String sayHello() {
        return "hello1";
    }
}
