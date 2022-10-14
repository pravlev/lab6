package com.lev_prav.common.util;

import java.io.Serializable;

public class ClientRequest implements Serializable {
    private final String commandName;
    private final String commandArguments;
    private final Serializable objectArgument;
    private final String username;
    private final String password;

    public ClientRequest(String commandName, String commandArguments, Serializable objectArgument, String username, String password) {
        this.commandName = commandName;
        this.commandArguments = commandArguments;
        this.objectArgument = objectArgument;
        this.username = username;
        this.password = password;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getCommandArguments() {
        return commandArguments;
    }

    public Serializable getObjectArgument() {
        return objectArgument;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "ClientRequest{"
                + " commandName='" + commandName + '\''
                + ", commandArguments='" + commandArguments + '\''
                + ", objectArgument=" + objectArgument
                + '}';
    }
}
