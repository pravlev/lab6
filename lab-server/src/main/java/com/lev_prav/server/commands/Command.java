package com.lev_prav.server.commands;

import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.exceptions.ScriptException;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.ServerResponse;

import java.io.FileNotFoundException;

/**
 * Этот класс родитель всех команд
 */

public abstract class Command {
    private final String name;
    private final String description;
    private CommandObjectRequirement objectRequirement;
    private final boolean commandNeedsStringArgument;

    public Command(String name, String description, CommandObjectRequirement requirement, boolean commandNeedsStringArgument) {
        this.name = name;
        this.description = description;
        this.objectRequirement = requirement;
        this.commandNeedsStringArgument = commandNeedsStringArgument;
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
    public abstract ServerResponse execute(String argument, Object object, String username) throws NoSuchCommandException, IllegalValueException;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public CommandObjectRequirement getObjectRequirement() {
        return objectRequirement;
    }

    public boolean isCommandNeedsStringArgument() {
        return commandNeedsStringArgument;
    }
}
