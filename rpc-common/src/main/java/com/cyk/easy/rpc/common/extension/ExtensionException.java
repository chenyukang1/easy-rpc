package com.cyk.easy.rpc.common.extension;

import java.io.Serial;

public class ExtensionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1044521700432293025L;

    public ExtensionException() {
        super();
    }

    public ExtensionException(String message) {
        super(message);
    }

    public ExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
