package com.cyk.easy.rpc.common.extension;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ExtensionLoaderTest {

    @Test
    public void test_extension_loader() {
        SPI1 spi1 = ExtensionLoader.getExtensionLoader(SPI1.class).getExtension("1");
        assertThat(spi1.sayHello() , equalTo("hello1"));

        SPI2 spi2 = ExtensionLoader.getExtensionLoader(SPI2.class).getExtension("1");
        assertThat(spi2.sayHello() , equalTo("hello2"));
    }
}
