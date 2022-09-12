package com.lev_prav.common.util;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    private static final long serialVersionUID = 1234567L;
    private final String message;
    private final ExecuteCode executeCode;

    public ServerResponse(String message, ExecuteCode executeCode) {
        this.message = message;
        this.executeCode = executeCode;
    }

    public ServerResponse(ExecuteCode executeCode) {
        this.message = "";
        this.executeCode = executeCode;
    }

    public String getMessage() {
        return message;
    }

    public ExecuteCode getExecuteCode() {
        return executeCode;
    }

    @Override
    public String toString() {
        return "ServerResponse{"
                + " message='" + message + '\''
                + ", executeCode=" + executeCode
                + '}';
    }
}
