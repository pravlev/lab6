package com.lev_prav.server.commands;

import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

public class GroupCountingByCreationDateCommand extends Command {

    private CollectionManager collectionManager;

    public GroupCountingByCreationDateCommand(CollectionManager collectionManager) {
        super("group_counting_by_creation_date", "сгруппировать элементы коллекции по значению поля creationDate, вывести количество элементов в каждой группе"
                , CommandRequirement.NONE);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException, IllegalValueException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        collectionManager.groupCountingByCreationDate().forEach((k, v) -> stringBuilder.append(k + ": " + v + '\n'));
        return new ServerResponse('\n' + stringBuilder.toString(), ExecuteCode.VALUE);
    }
}
