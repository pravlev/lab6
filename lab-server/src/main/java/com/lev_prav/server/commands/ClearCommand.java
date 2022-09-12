package com.lev_prav.server.commands;


import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

/**
 * Этот класс реализует команду Clear
 */
public class ClearCommand extends Command {
    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию", CommandRequirement.NONE);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException, IllegalValueException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        collectionManager.clear();

        return new ServerResponse(ExecuteCode.SUCCESS);
    }
}
