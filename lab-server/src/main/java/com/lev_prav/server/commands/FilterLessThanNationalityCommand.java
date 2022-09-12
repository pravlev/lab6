package com.lev_prav.server.commands;


import com.lev_prav.common.data.Country;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.util.CollectionManager;

public class FilterLessThanNationalityCommand extends Command {
    private final CollectionManager collectionManager;

    public FilterLessThanNationalityCommand(CollectionManager collectionManager) {
        super("filter_less_than_nationality", "вывести элементы, значение поля nationality которых меньше заданного", CommandRequirement.NATIONALITY);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException {
        if (argument.isEmpty() || object != null ) {
            throw new NoSuchCommandException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        collectionManager.filerLessThanNationality((Country) object).stream().forEachOrdered((p) -> stringBuilder.append(p).append('\n'));
        return new ServerResponse(stringBuilder.toString(), ExecuteCode.VALUE);
    }
}
