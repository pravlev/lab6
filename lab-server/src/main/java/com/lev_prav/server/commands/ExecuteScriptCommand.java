package com.lev_prav.server.commands;

import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;

public class ExecuteScriptCommand extends Command {

    public ExecuteScriptCommand() {
        super("execute_script", "считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме", CommandRequirement.NONE);
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException {
        if (argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }

        return new ServerResponse(argument, ExecuteCode.READ_SCRIPT);
    }
}
