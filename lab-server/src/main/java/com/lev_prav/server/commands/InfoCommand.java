package com.lev_prav.server.commands;


import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

public class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции", CommandRequirement.NONE);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException {
        if (!argument.isEmpty()) {
            throw new NoSuchCommandException();
        }
        String string = new String();

        string += "Информация о коллекции: \n";
        string += "тип: " + collectionManager.getPersons().getClass().getName() + '\n';
        string += "дата создания: " + collectionManager.getTimeCreate() + '\n';
        return new ServerResponse(string, ExecuteCode.VALUE);
    }
}
