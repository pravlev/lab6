package com.lev_prav.server.commands;


import com.lev_prav.common.data.Country;
import com.lev_prav.common.data.Person;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.collectionmanagers.CollectionManager;

import java.util.Comparator;
import java.util.Locale;

public class FilterLessThanNationalityCommand extends Command {
    private final CollectionManager collectionManager;

    public FilterLessThanNationalityCommand(CollectionManager collectionManager) {
        super("filter_less_than_nationality", "вывести элементы, значение поля nationality которых меньше заданного", CommandObjectRequirement.NONE, true);
        this.collectionManager = collectionManager;
    }

    @Override
    public ServerResponse execute(String argument, Object object, String username) throws NoSuchCommandException {
        if (argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        Country country = null;
        try {
            country = Country.valueOf(argument.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new NoSuchCommandException("Chose anything from list: UNITED_KINGDOM,GERMANY,SPAIN,INDIA,THAILAND");
        }
        collectionManager.filerLessThanNationality(country).stream().sorted(Comparator.comparing(Person::getLocation)).forEachOrdered((p) -> stringBuilder.append(p).append('\n'));
        return new ServerResponse(stringBuilder.toString(), ExecuteCode.VALUE);
    }
}
