package com.lev_prav.common.exceptions;

public class NoSuchCommandException extends Exception {

    public String getMessage() {
        return "Command with wrong argument. Type \"help\" to get all commands with their name and description";
    }
}

