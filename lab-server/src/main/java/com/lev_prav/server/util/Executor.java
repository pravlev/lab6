package com.lev_prav.server.util;

import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.commands.Command;

public class Executor {
    private final CommandManager commandManager;

    public Executor(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public ServerResponse executeCommand(String commandName, String argument, Object objectArgument, String username) {
        ServerResponse response;
        if (commandManager.getCommands().containsKey(commandName)) {
            Command command = commandManager.getCommands().get(commandName);
            try {
                response = command.execute(argument, objectArgument, username);
            } catch (NoSuchCommandException | IllegalValueException e) {
                response = new ServerResponse(e.getMessage(), ExecuteCode.ERROR);
            }
        } else {
            response = new ServerResponse("Unknown command detected: " + commandName, ExecuteCode.ERROR);
        }
        return response;
    }
}
