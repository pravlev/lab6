package com.lev_prav.server.commands;


import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

public class RemoveByIdCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "удалить элемент из коллекции по его id", CommandRequirement.NONE);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException, IllegalValueException {
        if (argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        if (!collectionManager.isId(Integer.parseInt(argument))) {
            throw new IllegalValueException("Элемента с данным id  нет в коллекции");
        }
        collectionManager.removeById(Integer.parseInt(argument));
        return new ServerResponse(ExecuteCode.SUCCESS);
    }
}
