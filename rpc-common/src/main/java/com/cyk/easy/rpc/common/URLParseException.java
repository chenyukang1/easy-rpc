package com.cyk.easy.rpc.common;

import java.io.Serial;

public class URLParseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 769493058898163513L;

    public URLParseException(String message) {
        super(message);
    }

    public URLParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
