package com.lev_prav.server.commands;


import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

public class RemoveLowerCommand extends Command {
    private CollectionManager collectionManager;

    public RemoveLowerCommand(CollectionManager collectionManager) {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", CommandRequirement.PERSON);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException{
        if (!argument.isEmpty() || object == null || object.getClass() != Person.class) {
            throw new NoSuchCommandException();
        }
        collectionManager.removeLower((Person) object);
        return new ServerResponse(ExecuteCode.SUCCESS);
    }
}
