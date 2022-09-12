package com.lev_prav.server.commands;


import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;

import java.util.HashMap;

public class HelpCommand extends Command {
    private HashMap<String, Command> commands;

    public HelpCommand( HashMap<String, Command> commands) {
        super("help", "вывести справку по доступным командам", CommandRequirement.NONE);
        this.commands = commands;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException {
        if (!argument.isEmpty()|| object != null) {
            throw new NoSuchCommandException();
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Список доступных команд:\n");
        commands.forEach((k, v) -> stringBuilder.append(k + ":" + v.getDescription() + '\n'));
        return new ServerResponse(stringBuilder.toString(), ExecuteCode.VALUE);
    }
}
