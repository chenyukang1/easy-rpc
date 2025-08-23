package com.cyk.easy.rpc.remoting.zookeeper;

import java.io.Serial;

public class RemotingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8123857285670754144L;

    public RemotingException(String message) {
        super(message);
    }

    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemotingException(Throwable cause) {
        super(cause);
    }

}
