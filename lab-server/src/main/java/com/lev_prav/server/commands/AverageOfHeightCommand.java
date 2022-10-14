package com.lev_prav.server.commands;

import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.collectionmanagers.CollectionManager;

/**
 * Этот класс реализует команду average_of_height
 */
public class AverageOfHeightCommand extends Command {
    private CollectionManager collectionManager;

    public AverageOfHeightCommand(CollectionManager collectionManager) {
        super("average_of_height", "вывести среднее значение поля height для всех элементов коллекции", CommandObjectRequirement.NONE, true);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object, String username) throws NoSuchCommandException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        return new ServerResponse(String.valueOf(collectionManager.averageOfHeight()), ExecuteCode.VALUE);
    }
}
