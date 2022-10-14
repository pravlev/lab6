package com.lev_prav.server.commands;

import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;

import java.util.logging.Logger;

public class ExitCommand extends Command {
    private final Logger logger;

    public ExitCommand(Logger logger) {
        super("exit", "завершить программу (без сохранения в файл)", CommandObjectRequirement.NONE, false);
        this.logger = logger;
    }

    @Override
    public ServerResponse execute(String argument, Object object, String username) throws NoSuchCommandException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        logger.info(() -> "user " + username + " logged out");
        return new ServerResponse(ExecuteCode.EXIT);
    }
}
