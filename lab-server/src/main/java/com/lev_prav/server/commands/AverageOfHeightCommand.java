package com.lev_prav.server.commands;

import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

/**
 * Этот класс реализует команду average_of_height
 */
public class AverageOfHeightCommand extends Command {
    private CollectionManager collectionManager;

    public AverageOfHeightCommand(CollectionManager collectionManager) {
        super("average_of_height", "вывести среднее значение поля height для всех элементов коллекции", CommandRequirement.NONE);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException, IllegalValueException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        return new ServerResponse(String.valueOf(collectionManager.averageOfHeight()), ExecuteCode.VALUE);
    }
}
