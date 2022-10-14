package com.lev_prav.server.commands;


import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.collectionmanagers.CollectionManager;

public class RemoveLowerCommand extends Command {
    private CollectionManager collectionManager;

    public RemoveLowerCommand(CollectionManager collectionManager) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", CommandObjectRequirement.PERSON, false);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object, String username) throws NoSuchCommandException {
        if (!argument.isEmpty() || object == null || object.getClass() != Person.class) {
            throw new NoSuchCommandException();
        }

        int undeletedItems = collectionManager.removeLower((Person) object, username);
        if (undeletedItems != 0) {
            return new ServerResponse(undeletedItems + " objects were not deleted", ExecuteCode.SERVER_ERROR);
        }
        return new ServerResponse(ExecuteCode.SUCCESS);
    }
}
