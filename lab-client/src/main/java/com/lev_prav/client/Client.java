package com.lev_prav.client;


import com.lev_prav.client.exceptions.InvalidInputException;
import com.lev_prav.client.exceptions.NoConnectionException;
import com.lev_prav.client.utility.Color;
import com.lev_prav.client.utility.ConsoleManager;
import com.lev_prav.client.utility.PersonFiller;
import com.lev_prav.client.utility.PersonReader;
import com.lev_prav.client.utility.Requester;
import com.lev_prav.client.utility.UserIO;
import com.lev_prav.common.exceptions.IllegalAddressException;
import com.lev_prav.common.util.Checker;
import com.lev_prav.common.util.CommandRequirement;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Scanner;

public final class Client {
    private static final int TIMEOUT = 100;
    private static final int BUFFER_SIZE = 3048;
    private static final int RECONNECTION_ATTEMPTS = 5;
    private static final int NUMBER_OF_ARGUMENTS = 2;

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        UserIO userIO = new UserIO(new Scanner(System.in), new PrintWriter(System.out));
        PersonReader personReader = new PersonReader(userIO);
        PersonFiller personFiller = new PersonFiller(personReader, userIO);
        if (args.length == NUMBER_OF_ARGUMENTS) {
            try (DatagramChannel client = DatagramChannel.open()) {
                InetSocketAddress serverAddress = Checker.checkAddress(args[0], args[1]);
                client.bind(null).configureBlocking(false);
                Requester requester = new Requester(client, serverAddress, TIMEOUT, BUFFER_SIZE, RECONNECTION_ATTEMPTS,
                        userIO);
                HashMap<String, CommandRequirement> requirements = requester.sendPullingRequest();
                ConsoleManager consoleManager = new ConsoleManager(requirements, userIO,
                        personFiller, requester);
                consoleManager.start();
            } catch (InvalidInputException | NoConnectionException | IllegalAddressException e) {
                userIO.writelnColorMessage(e.getMessage(), Color.RED);
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
                userIO.writelnColorMessage("error during connection:", Color.RED);
                e.printStackTrace();
            }
        } else {
            userIO.writelnColorMessage("please enter a server hostname and port as a command "
                    + "line arguments", Color.RED);
        }
    }
}
