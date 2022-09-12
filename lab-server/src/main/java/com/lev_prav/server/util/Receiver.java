package com.lev_prav.server.util;


import com.lev_prav.common.exceptions.CSVException;
import com.lev_prav.common.exceptions.IllegalValueException;
import com.lev_prav.common.exceptions.NoSuchCommandException;
import com.lev_prav.common.util.ClientRequest;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.PullingRequest;
import com.lev_prav.common.util.PullingResponse;
import com.lev_prav.common.util.Serializer;
import com.lev_prav.common.util.ServerResponse;
import com.lev_prav.server.commands.Command;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Receiver {
    private final int bufferSize;
    private final CommandManager commandManager;
    private final DatagramSocket server;
    private final Logger logger;

    public Receiver(CommandManager commandManager, DatagramSocket server, int bufferSize, Logger logger) {
        this.commandManager = commandManager;
        this.server = server;
        this.bufferSize = bufferSize;
        this.logger = logger;
    }

    public void receive() throws IOException, ClassNotFoundException {
        byte[] bytesReceiving = new byte[bufferSize];
        DatagramPacket request = new DatagramPacket(bytesReceiving, bytesReceiving.length);
        server.receive(request);
        Object received = Serializer.deserialize(bytesReceiving);
        InetAddress client = request.getAddress();
        int port = request.getPort();
        logger.info("received request from address " + client + ", port " + port);
        Object response;
        if (received instanceof PullingRequest) {
            response = new PullingResponse(commandManager.getRequirements());
        } else {
            ClientRequest clientRequest = (ClientRequest) received;
            String inputCommand = clientRequest.getCommandName();
            String argument = clientRequest.getCommandArguments();
            Serializable objectArgument = clientRequest.getObjectArgument();
            if (commandManager.getCommands().containsKey(inputCommand)) {
                Command command = commandManager.getCommands().get(inputCommand);
                try {
                    response = command.execute(argument, objectArgument);
                    commandManager.addToHistory(command);
                    commandManager.getSaveCommand().execute("", null);
                } catch (NoSuchCommandException | IllegalValueException e) {
                    response = new ServerResponse(e.getMessage(), ExecuteCode.ERROR);
                } catch (CSVException e) {
                    response = new ServerResponse("Error during converting data to file", ExecuteCode.ERROR);
                }
            } else {
                response = new ServerResponse("Unknown command detected: " + inputCommand, ExecuteCode.ERROR);
            }
        }
        byte[] bytesSending = Serializer.serialize(response);
        DatagramPacket packet = new DatagramPacket(bytesSending, bytesSending.length, client, port);
        server.send(packet);
        logger.info("response sent to the address " + client + ", port " + port);
    }
}
