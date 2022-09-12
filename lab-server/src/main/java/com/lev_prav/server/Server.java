package com.lev_prav.server;


import com.lev_prav.common.exceptions.CSVException;
import com.lev_prav.common.exceptions.IllegalAddressException;
import com.lev_prav.common.util.Checker;
import com.lev_prav.server.util.CollectionManager;
import com.lev_prav.server.util.CommandManager;
import com.lev_prav.server.util.ParserCSV;
import com.lev_prav.server.util.Receiver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public final class Server {
    private static final int BUFFER_SIZE = 2048;
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static final int NUMBER_OF_ARGUMENTS = 3;

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        LOGGER.trace("the server is running");
        if (args.length == NUMBER_OF_ARGUMENTS) {
            try {
                //the host name is indicated by the first string in the command line arguments, the port - by second
                final InetSocketAddress address = Checker.checkAddress(args[0], args[1]);
                LOGGER.info(() -> "set " + address + " address");
                // the filename is indicated by the third string in the command line arguments
                final String filename = args[2].trim();
                if (filename.isEmpty()) {
                    LOGGER.error("no data file!");
                } else {
                    try {
                        DatagramSocket server = new DatagramSocket(address);
                        CollectionManager collectionManager = ParserCSV.parseCSVToCollection(filename, LOGGER);
                        CommandManager commandManager = new CommandManager(collectionManager,filename);
                        Receiver receiver = new Receiver(commandManager, server, BUFFER_SIZE, LOGGER);
                        while (true) {
                            receiver.receive();
                        }
                    } catch (ClassNotFoundException e) {
                        LOGGER.error("wrong data from client");
                    } catch (CSVException e) {
                        LOGGER.error(() -> "Error during converting xml " + filename + " to java object", e);
                    } catch (IOException e) {
                        LOGGER.error(e);
                    }
                }
            } catch (IllegalAddressException e) {
                LOGGER.error(e.getMessage());
            }
        } else {
            LOGGER.error("command line arguments must indicate host name, port and filename");
        }
        LOGGER.trace("the server is shutting down");
    }
}
