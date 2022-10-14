package com.lev_prav.server.util;


import com.lev_prav.common.util.ClientRequest;
import com.lev_prav.common.util.ExecuteCode;
import com.lev_prav.common.util.PullingRequest;
import com.lev_prav.common.util.Serializer;
import com.lev_prav.common.util.ServerResponse;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class Receiver {
    private final int bufferSize;
    private final DatagramSocket server;
    private final Logger logger;
    private final Executor executor;
    private final UsersHandler usersHandler;

    public Receiver(DatagramSocket server, int bufferSize, Logger logger, Executor executor,
                    UsersHandler usersHandler) {
        this.server = server;
        this.bufferSize = bufferSize;
        this.logger = logger;
        this.executor = executor;
        this.usersHandler = usersHandler;
    }

    public void start(ExecutorService requestReadingPool, ExecutorService requestProcessingPool,
                      ExecutorService responseSendingPool) {
        requestReadingPool.submit(() -> {
            while (!server.isClosed()) {
                ReceivedData receivedData = receive();
                Object request = receivedData.getRequest();
                InetAddress client = receivedData.getClient();
                int port = receivedData.getPort();
                requestProcessingPool.submit(() -> {
                    Object response = processRequest(request);
                    responseSendingPool.submit(() -> sendResponse(response, client, port));
                });
            }
        });
    }

    private Object processRequest(Object received) {
        if (received instanceof PullingRequest) {
            return usersHandler.handle((PullingRequest) received);
        } else {
            ClientRequest clientRequest = (ClientRequest) received;
            User user = new User(clientRequest.getUsername(), PasswordEncoder.encode(clientRequest.getPassword()));
            if (usersHandler.checkUser(user)) {
                return executor.executeCommand(clientRequest.getCommandName(), clientRequest.getCommandArguments(),
                        clientRequest.getObjectArgument(), user.getUsername());
            } else {
                return new ServerResponse("commands can only be executed by authorized users", ExecuteCode.ERROR);
            }
        }
    }

    private boolean sendResponse(Object response, InetAddress client, int port) {
        try {
            byte[] bytesSending = Serializer.serialize(response);
            DatagramPacket packet = new DatagramPacket(bytesSending, bytesSending.length, client, port);
            server.send(packet);
            logger.info("response sent to the address " + client + ", port " + port);
        } catch (IOException e) {
            logger.severe("error during sending response " + e.getMessage());
        }
        return true;
    }

    private ReceivedData receive() {
        ReceivedData receivedData = null;
        try {
            byte[] bytesReceiving = new byte[bufferSize];
            DatagramPacket request = new DatagramPacket(bytesReceiving, bytesReceiving.length);
            server.receive(request);
            Object received = Serializer.deserialize(bytesReceiving);
            InetAddress client = request.getAddress();
            int port = request.getPort();
            receivedData = new ReceivedData(received, client, port);
            logger.info(() -> "received request from address " + client + ", port " + port);
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("error during reading response " + e.getMessage());
        }
        return receivedData;
    }
}