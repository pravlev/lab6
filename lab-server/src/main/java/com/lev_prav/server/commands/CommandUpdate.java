package com.lev_prav.server.commands;

import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.collectionmanagers.CollectionManager;

/**
 * Этот класс реализует команду update
 */
public class CommandUpdate extends Command {
    private final CollectionManager collectionManager;

    public CommandUpdate(CollectionManager collectionManager) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному(элемент нужно вводить после запуска команды)", CommandObjectRequirement.PERSON, true);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object, String username) throws NoSuchCommandException, IllegalValueException {
        if (argument.isEmpty() || object == null || object.getClass() != Person.class) {
            throw new NoSuchCommandException();
        }
        try {
            int id = Integer.parseInt(argument);
            if (!collectionManager.containsId(id)) {
                throw new IllegalValueException("There's no value with that id.");
            }
            if (!collectionManager.getById(id).getOwnerUserName().equals(username)) {
                throw new IllegalValueException("Object with that key belong to the another user");
            }
            if (!collectionManager.updatePerson(id, (Person) object)) {
                return new ServerResponse("Cannot update element", ExecuteCode.SERVER_ERROR);
            }
            return new ServerResponse(ExecuteCode.SUCCESS);
        } catch (NumberFormatException e) {
            return new ServerResponse("the argument must be a long number", ExecuteCode.ERROR);
        }
    }
}
