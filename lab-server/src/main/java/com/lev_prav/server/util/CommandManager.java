package com.lev_prav.server.util;


import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.server.commands.AverageOfHeightCommand;
import com.lev_prav.server.commands.ClearCommand;
import com.lev_prav.server.commands.Command;
import com.lev_prav.server.commands.CommandAdd;
import com.lev_prav.server.commands.CommandUpdate;
import com.lev_prav.server.commands.ExecuteScriptCommand;
import com.lev_prav.server.commands.ExitCommand;
import com.lev_prav.server.commands.FilterLessThanNationalityCommand;
import com.lev_prav.server.commands.GroupCountingByCreationDateCommand;
import com.lev_prav.server.commands.HeadCommand;
import com.lev_prav.server.commands.HelpCommand;
import com.lev_prav.server.commands.HistoryCommand;
import com.lev_prav.server.commands.InfoCommand;
import com.lev_prav.server.commands.RemoveByIdCommand;
import com.lev_prav.server.commands.RemoveLowerCommand;
import com.lev_prav.server.commands.SaveCommand;
import com.lev_prav.server.commands.ShowCommand;

import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final HistoryCommand historyCommand;
    private final HashMap<String, CommandRequirement> requirements = new HashMap<>();
    private final SaveCommand saveCommand;

    public CommandManager(CollectionManager collectionManager, String fileName) {
        ClearCommand clearCommand = new ClearCommand(collectionManager);
        commands.put(clearCommand.getName(), clearCommand);
        CommandAdd commandAdd = new CommandAdd(collectionManager);
        commands.put(commandAdd.getName(), commandAdd);
        CommandUpdate commandUpdate = new CommandUpdate(collectionManager);
        commands.put(commandUpdate.getName(), commandUpdate);
        ExecuteScriptCommand executeScriptCommand = new ExecuteScriptCommand();
        commands.put(executeScriptCommand.getName(), executeScriptCommand);
        ExitCommand exitCommand = new ExitCommand(collectionManager);
        commands.put(exitCommand.getName(), exitCommand);
        HeadCommand headCommand = new HeadCommand(collectionManager);
        commands.put(headCommand.getName(), headCommand);
        HelpCommand helpCommand = new HelpCommand(commands);
        commands.put(helpCommand.getName(), helpCommand);
        historyCommand = new HistoryCommand();
        commands.put(historyCommand.getName(), historyCommand);
        InfoCommand infoCommand = new InfoCommand(collectionManager);
        commands.put(infoCommand.getName(), infoCommand);
        RemoveByIdCommand removeByIdCommand = new RemoveByIdCommand(collectionManager);
        commands.put(removeByIdCommand.getName(), removeByIdCommand);
        RemoveLowerCommand removeLowerCommand = new RemoveLowerCommand(collectionManager);
        commands.put(removeLowerCommand.getName(), removeLowerCommand);
        ShowCommand showCommand = new ShowCommand(collectionManager);
        commands.put(showCommand.getName(), showCommand);
        AverageOfHeightCommand averageOfHeightCommand = new AverageOfHeightCommand(collectionManager);
        commands.put(averageOfHeightCommand.getName(), averageOfHeightCommand);
        GroupCountingByCreationDateCommand groupCountingByCreationDateCommand = new GroupCountingByCreationDateCommand(collectionManager);
        commands.put(groupCountingByCreationDateCommand.getName(), groupCountingByCreationDateCommand);
        FilterLessThanNationalityCommand filterLessThanNationalityCommand = new FilterLessThanNationalityCommand(collectionManager);
        commands.put(filterLessThanNationalityCommand.getName(), filterLessThanNationalityCommand);

        commands.forEach((k, v) -> requirements.put(k, v.getRequirement()));
        saveCommand = new SaveCommand(collectionManager);
    }

    public void addToHistory(Command command) {
        historyCommand.add(command);
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public HashMap<String, CommandRequirement> getRequirements() {
        return requirements;
    }

    public SaveCommand getSaveCommand() {
        return saveCommand;
    }
}
