package com.lev_prav.client.utility;

import com.lev_prav.client.exceptions.InvalidInputException;
import com.lev_prav.client.exceptions.NoConnectionException;
import com.lev_prav.common.exceptions.ScriptException;
import com.lev_prav.common.util.ClientRequest;
import com.lev_prav.common.util.CommandObjectRequirement;
import com.lev_prav.common.util.CommandRequirement;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.PullingResponse;
import com.lev_prav.common.util.RegistrationCode;
import com.lev_prav.common.util.ServerResponse;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

public class ConsoleManager {
    private HashMap<String, CommandRequirement> commands;
    private final UserIO userIO;
    private final Requester requester;
    private final PersonFiller personFiller;
    private String username;
    private String password;

    public ConsoleManager(UserIO userIO, PersonFiller personFiller, Requester requester) {
        this.userIO = userIO;
        this.personFiller = personFiller;
        this.requester = requester;
    }

    /**
     * starts read commands and execute it while it is not an exit command
     */
    public void start() throws IOException, ClassNotFoundException, InvalidInputException, NoConnectionException, InterruptedException {
        authorize();
        boolean executeFlag = true;
        while (executeFlag) {
            String input = userIO.readline();
            if (!input.trim().isEmpty()) {
                String inputCommand = input.trim().split(" ")[0].toLowerCase(Locale.ROOT);
                String argument = "";
                if (input.split(" ").length > 1) {
                    argument = input.replaceFirst(inputCommand + " ", "");
                }
                try {
                    ClientRequest request = new ClientRequest(inputCommand, argument, getObjectArgument(inputCommand, argument), username, password);
                    ServerResponse response = (ServerResponse) requester.send(request);
                    executeFlag = processServerResponse(response);
                } catch (ScriptException e) {
                    userIO.finishReadAllScript();
                    userIO.writelnColorMessage(e.getMessage(), Color.RED);
                }
            } else {
                userIO.writelnColorMessage("Please type any command. To see list of command type \"help\"",
                        Color.RED);
            }
        }
    }

    public Serializable getObjectArgument(String commandName, String argument) throws ScriptException, InvalidInputException {
        Serializable object = null;
        if (commands.containsKey(commandName) && commands.get(commandName).isCommandNeedsStringArgument() == !argument.isEmpty()) {
            CommandObjectRequirement requirement = commands.get(commandName).getCommandObjectRequirement();
            switch (requirement) {
                case PERSON:
                    object = personFiller.fillPerson(username);
                    break;
                case NATIONALITY:
                    object = personFiller.fillNationality();
                    break;
                default:
                    break;
            }
        }
        return object;
    }

    /**
     * process the ExecuteCode of ServerResponse. print messages and finish read script if there's an error
     * @param serverResponse received response
     * @return false if it was exit command, true otherwise
     */
    public boolean processServerResponse(ServerResponse serverResponse) {
        ExecuteCode executeCode = serverResponse.getExecuteCode();
        switch (executeCode) {
            case ERROR:
                userIO.finishReadAllScript();
                userIO.writelnColorMessage(executeCode.getMessage(), Color.RED);
                userIO.writelnColorMessage(serverResponse.getMessage(), Color.RED);
                break;
            case SUCCESS:
                userIO.writelnColorMessage(executeCode.getMessage(), Color.GREEN);
                break;
            case VALUE:
                userIO.writeln(executeCode.getMessage());
                userIO.writeln(serverResponse.getMessage());
                break;
            case READ_SCRIPT:
                userIO.startReadScript(serverResponse.getMessage());
                break;
            case SERVER_ERROR:
                userIO.writelnColorMessage(executeCode.getMessage(), Color.RED);
                if (serverResponse.getMessage() != null) {
                    userIO.writelnColorMessage("cause:", Color.RED);
                    userIO.writelnColorMessage(serverResponse.getMessage(), Color.RED);
                }
                break;
            case EXIT:
                userIO.writelnColorMessage(executeCode.getMessage(), Color.RED);
                return false;
            default:
                userIO.writelnColorMessage("incorrect server's response...", Color.RED);
        }
        return true;
    }

    private void authorize() throws InvalidInputException, NoConnectionException, IOException, InterruptedException,
            ClassNotFoundException {
        boolean isAuthorized = false;
        do {
            userIO.write("enter username:");
            String newUsername = userIO.readline().trim();
            userIO.write("enter password:");
            String newPassword = userIO.readline().trim();
            PullingResponse response = requester.sendPullingRequest(newUsername, newPassword);
            if (response.getRegistrationCode() == RegistrationCode.AUTHORIZED
                    || response.getRegistrationCode() == RegistrationCode.REGISTERED) {
                isAuthorized = true;
                commands = response.getRequirements();
                username = newUsername;
                password = newPassword;
            }
            userIO.writeln(response.getRegistrationCode().getMessage());
        } while (!isAuthorized);
    }
}
