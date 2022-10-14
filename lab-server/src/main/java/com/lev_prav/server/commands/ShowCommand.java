package com.lev_prav.server.commands;


import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.collectionmanagers.CollectionManager;

import java.util.ArrayDeque;
import java.util.Comparator;

public class ShowCommand extends Command {
    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", CommandObjectRequirement.NONE, false);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object, String username) throws NoSuchCommandException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        ArrayDeque<Person> persons = collectionManager.getPersons();
        StringBuilder stringBuilder = new StringBuilder();
        if (persons.size() != 0) {
            stringBuilder.append("\nВсе элементы коллекции: \n");
            persons.stream().sorted(Comparator.comparing(Person::getLocation)).forEachOrdered((p) -> stringBuilder.append(p).append('\n'));
        } else {
            stringBuilder.append("collection is empty'\n");
        }
        return new ServerResponse(stringBuilder.toString(), ExecuteCode.VALUE);
    }
}
