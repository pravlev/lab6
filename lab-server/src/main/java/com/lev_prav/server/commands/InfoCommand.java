package com.lev_prav.server.commands;


import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.collectionmanagers.CollectionManager;

public class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции", CommandObjectRequirement.NONE, false);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object, String username) throws NoSuchCommandException {
        if (!argument.isEmpty()) {
            throw new NoSuchCommandException();
        }
        String string = "";

        string += "Информация о коллекции: \n";
        string += "тип: " + collectionManager.getPersons().getClass().getName() + '\n';
        string += "дата создания: " + collectionManager.getTimeCreate() + '\n';
        return new ServerResponse(string, ExecuteCode.VALUE);
    }
}
