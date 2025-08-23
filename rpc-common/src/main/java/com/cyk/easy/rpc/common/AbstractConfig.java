package com.cyk.easy.rpc.common;

import java.io.Serializable;

public abstract class AbstractConfig implements Serializable {



    /**
     * Username to login the register center.
     */
    protected String username;

    /**
     * Password to login the register center.
     */
    private String password;

}
