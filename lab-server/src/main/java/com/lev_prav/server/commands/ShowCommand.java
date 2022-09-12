package com.lev_prav.server.commands;


import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

import java.util.ArrayDeque;

public class ShowCommand extends Command {
    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", CommandRequirement.NONE);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        ArrayDeque<Person> persons = collectionManager.getPersons();
        StringBuilder stringBuilder = new StringBuilder();
        if (persons.size() != 0) {
            stringBuilder.append("\nВсе элементы коллекции: \n");
            persons.stream().forEachOrdered((p) -> stringBuilder.append(p.toString()).append('\n'));
        } else {
            stringBuilder.append("collection is empty'\n");
        }
        return new ServerResponse(stringBuilder.toString(), ExecuteCode.VALUE);
    }
}
