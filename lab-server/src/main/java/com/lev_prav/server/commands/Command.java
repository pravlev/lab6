package com.lev_prav.server.commands;

import com.lev_prav.common.exceptions.CSVException;
import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.exceptions.ScriptException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ServerResponse;

import java.io.FileNotFoundException;

/**
 * Этот класс родитель всех команд
 */

public abstract class Command {
    private final String name;
    private final String description;
    private CommandRequirement requirement;

    public Command(String name, String description, CommandRequirement requirement) {
        this.name = name;
        this.description = description;
        this.requirement = requirement;
    }

    /**
     * Выполняет команду
     *
     * @param argument
     * @param object
     * @throws NoSuchCommandException
     * @throws ScriptException
     * @throws IllegalValueException
     * @throws FileNotFoundException
     */
    public abstract ServerResponse execute(String argument, Object object) throws NoSuchCommandException, IllegalValueException, FileNotFoundException, CSVException;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public CommandRequirement getRequirement() {
        return requirement;
    }
}
