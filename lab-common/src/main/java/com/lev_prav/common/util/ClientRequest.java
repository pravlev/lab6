package com.lev_prav.common.util;

import java.io.Serializable;

public class ClientRequest implements Serializable {
    private final String commandName;
    private final String commandArguments;
    private final Serializable objectArgument;

    public ClientRequest(String commandName, String commandArguments, Serializable objectArgument) {
        this.commandName = commandName;
        this.commandArguments = commandArguments;
        this.objectArgument = objectArgument;
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

    @Override
    public String toString() {
        return "ClientRequest{"
                + " commandName='" + commandName + '\''
                + ", commandArguments='" + commandArguments + '\''
                + ", objectArgument=" + objectArgument
                + '}';
    }
}
