package com.lev_prav.server.commands;

import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

/**
 * Этот класс реализует команду update
 */
public class CommandUpdate extends Command {
    private final CollectionManager collectionManager;

    public CommandUpdate(CollectionManager collectionManager) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному(элемент нужно вводить после запуска команды)", CommandRequirement.PERSON);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException, IllegalValueException {
        if (argument.isEmpty() || object == null || object.getClass() != Person.class) {
            throw new NoSuchCommandException();
        }
        if (!collectionManager.isId(Integer.parseInt(argument))) {
            throw new IllegalValueException("Элемента с данным id  нет в коллекции");
        }

        if(collectionManager.updatePerson(Integer.parseInt(argument), (Person) object)) {
            return new ServerResponse(ExecuteCode.SUCCESS);
        }
        return new ServerResponse("Данный passportId уже есть у другого человека или нет человека с данным Id", ExecuteCode.ERROR);
    }
}
