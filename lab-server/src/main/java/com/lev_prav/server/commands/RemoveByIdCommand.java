package com.lev_prav.server.commands;


import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.collectionmanagers.CollectionManager;

public class RemoveByIdCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "удалить элемент из коллекции по его id", CommandObjectRequirement.NONE, false);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object, String username) throws NoSuchCommandException, IllegalValueException {
        if (argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        try {
            int id = Integer.parseInt(argument);
            if (!collectionManager.isId(id)) {
                throw new IllegalValueException("There's no object with that key.");
            }
            if (!collectionManager.getById(id).getOwnerUserName().equals(username)) {
                throw new IllegalValueException("Object with that key belong to the another user");
            }
            if (!collectionManager.removeById(id)) {
                return new ServerResponse("Cannot remove object", ExecuteCode.SERVER_ERROR);
            }
            return new ServerResponse(ExecuteCode.SUCCESS);
        } catch (NumberFormatException e) {
            return new ServerResponse("the argument must be a long number", ExecuteCode.ERROR);
        }
    }
}
