package com.lev_prav.common.exceptions;

public class NoSuchCommandException extends Exception {
    private String s;

    public NoSuchCommandException() {
        s = "Command with wrong argument. Type \"help\" to get all commands with their name and description";
    }

    public NoSuchCommandException(String s) {
        this.s = s;
    }

    public String getMessage() {
        return s;
    }

}

