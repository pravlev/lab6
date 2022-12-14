package com.lev_prav.server.commands;


import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.collectionmanagers.CollectionManager;

public class HeadCommand extends Command {

    private CollectionManager collectionManager;

    public HeadCommand(CollectionManager collectionManager) {
        super("head", "вывести первый элемент коллекции", CommandObjectRequirement.NONE, false);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object, String username) throws NoSuchCommandException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        if (collectionManager.getPersons().isEmpty()) {
            return new ServerResponse("collection is empty", ExecuteCode.VALUE);
        }
        return new ServerResponse(collectionManager.getPersons().getFirst().toString(), ExecuteCode.VALUE);
    }
}
