package com.lev_prav.common.exceptions;

public class ScriptException extends Exception {

    public String getMessage() {
        return "Error during script execution";
    }
}
