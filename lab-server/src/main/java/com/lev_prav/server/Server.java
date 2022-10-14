package com.lev_prav.server;


import com.lev_prav.common.exceptions.IllegalAddressException;
import com.lev_prav.common.util.Checker;
import com.lev_prav.server.collectionmanagers.CollectionManager;
import com.lev_prav.server.collectionmanagers.SQLDataManager;
import com.lev_prav.server.usersmanagers.SQLUserManager;
import com.lev_prav.server.usersmanagers.SQLUserTableCreator;
import com.lev_prav.server.util.CommandManager;
import com.lev_prav.server.util.Executor;
import com.lev_prav.server.util.Receiver;
import com.lev_prav.server.util.UsersHandler;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public final class Server {
    private static final int BUFFER_SIZE = 2048;
    private static final Logger LOGGER = Logger.getLogger(Server.class.toString());
    private static final int NUMBER_OF_ARGUMENTS = 7;
    private static final int INDEX_HOST = 0;
    private static final int INDEX_PORT = 1;
    private static final int INDEX_DB_HOSTNAME = 2;
    private static final int INDEX_DB_PORT = 3;
    private static final int INDEX_DB_NAME = 4;
    private static final int INDEX_DB_USERNAME = 5;
    private static final int INDEX_DB_PASSWORD = 6;
    private static final String USER_TABLE_NAME = "personsusers";
    private static final String DATA_TABLE_NAME = "persons";
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ExecutorService REQUEST_READING_POOL = Executors.newCachedThreadPool();
    private static final ExecutorService REQUEST_PROCESSING_POOL = Executors.newFixedThreadPool(10);
    private static final ExecutorService RESPONSE_SENDING_POOL = Executors.newFixedThreadPool(10);

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        LOGGER.fine("the server is running");
        if (args.length != NUMBER_OF_ARGUMENTS) {
            LOGGER.severe("command line arguments must indicate host name, port, database host name, port and name, username and password in this order ");
            return;
        }
        try {
            final InetSocketAddress address = Checker.checkAddress(args[INDEX_HOST], args[INDEX_PORT]);
            final String dataBaseUrl = "jdbc:postgresql://" + args[INDEX_DB_HOSTNAME] + ":" + args[INDEX_DB_PORT] + "/" + args[INDEX_DB_NAME];
            final String dataBaseUsername = args[INDEX_DB_USERNAME];
            final String dataBasePassword = args[INDEX_DB_PASSWORD];
            try {
                Class.forName("org.postgresql.Driver");
                Connection connection = DriverManager.getConnection(dataBaseUrl, dataBaseUsername, dataBasePassword);
                DatagramSocket server = new DatagramSocket(address);
                LOGGER.info(() -> "connected to the database " + dataBaseUrl);
                LOGGER.info(() -> "opened datagram socket on the address " + address);
                SQLDataManager sqlDataManager = new SQLDataManager(connection, DATA_TABLE_NAME, USER_TABLE_NAME, LOGGER);
                SQLUserTableCreator sqlUserTableCreator = new SQLUserTableCreator(connection, USER_TABLE_NAME, LOGGER);
                SQLUserManager sqlUserManager = new SQLUserManager(new ReentrantLock(), sqlUserTableCreator.init(), connection, USER_TABLE_NAME, LOGGER);
                CollectionManager collectionManager = new CollectionManager(sqlDataManager.initCollection(), sqlDataManager);
                CommandManager commandManager = new CommandManager(collectionManager, LOGGER);
                Executor executor = new Executor(commandManager);
                UsersHandler usersHandler = new UsersHandler(sqlUserManager, commandManager.getRequirements(), LOGGER);
                Receiver receiver = new Receiver(server, BUFFER_SIZE, LOGGER, executor, usersHandler);
                receiver.start(REQUEST_READING_POOL, REQUEST_PROCESSING_POOL, RESPONSE_SENDING_POOL);
                while (true) {
                    if (exitFromConsole()) {
                        REQUEST_READING_POOL.shutdown();
                        REQUEST_PROCESSING_POOL.shutdown();
                        RESPONSE_SENDING_POOL.shutdown();
                        break;
                    }
                }
            } catch (IOException | SQLException | ClassNotFoundException e) {
                LOGGER.severe(e.getMessage());
            }
        } catch (IllegalAddressException e) {
            LOGGER.severe(e.getMessage());
        }
        LOGGER.fine("the server is shutting down");
    }

    private static boolean exitFromConsole() {
        if (!SCANNER.hasNext()) {
            return false;
        }
        String command = SCANNER.nextLine().toLowerCase(Locale.ROOT);
        if ("exit".equals(command)) {
            return true;
        }
        System.out.println("unknown command");
        return false;
    }
}