package com.lev_prav.server.commands;

import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.collectionmanagers.CollectionManager;

/**
 * Этот класс реализует команду add
 */
public class CommandAdd extends Command {
    private final CollectionManager collectionManager;

    public CommandAdd(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию(элемент нужно вводить после запуска команды)", CommandObjectRequirement.PERSON, false);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object person, String username) throws NoSuchCommandException {
        if (!argument.isEmpty() || person == null || person.getClass() != Person.class) {
            throw new NoSuchCommandException();
        }
        if (collectionManager.addNewPerson((Person) person)) {
            return new ServerResponse(ExecuteCode.SUCCESS);
        }
        return new ServerResponse("Данный passportId уже есть у другого человека", ExecuteCode.ERROR);
    }
}
