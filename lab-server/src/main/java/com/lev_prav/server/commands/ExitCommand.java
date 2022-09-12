package com.lev_prav.server.commands;

import com.lev_prav.common.exceptions.CSVException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

public class ExitCommand extends Command {
    private final CollectionManager collectionManager;

    public ExitCommand(CollectionManager collectionManager) {
        super("exit", "завершить программу (без сохранения в файл)", CommandRequirement.NONE);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException, CSVException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        collectionManager.save();
        return new ServerResponse(ExecuteCode.EXIT);
    }
}
