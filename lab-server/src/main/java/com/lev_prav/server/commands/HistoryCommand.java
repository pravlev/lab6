package com.lev_prav.server.commands;


import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.ServerResponse;

import java.util.ArrayDeque;
import java.util.Queue;

public class HistoryCommand extends Command {
    private final int numberOfElements = 10;
    private final Queue<Command> history = new ArrayDeque<>();

    public HistoryCommand() {
        super("history", "вывести последние 10 команд (без их аргументов)", CommandRequirement.NONE);
    }

    @Override
    public ServerResponse execute(String argument, Object object) throws NoSuchCommandException {
        if (!argument.isEmpty() || object != null) {
            throw new NoSuchCommandException();
        }

        StringBuilder stringBuilder = new StringBuilder();
        history.forEach(e -> stringBuilder.append(e.getName()).append('\n'));
        return new ServerResponse(stringBuilder.toString(), ExecuteCode.VALUE);
    }

    public void add(Command command) {
        if (history.size() == numberOfElements) {
            history.poll();
        }
        history.add(command);
    }
}
