package com.lev_prav.server.commands;


import com.lev_prav.common.exceptions.CSVException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

import java.io.FileNotFoundException;

public class SaveCommand extends Command {
    private CollectionManager collectionManager;

    public SaveCommand(CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл", CommandRequirement.NONE);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException, FileNotFoundException, CSVException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        collectionManager.save();
        return new ServerResponse(ExecuteCode.SUCCESS);
    }
}
