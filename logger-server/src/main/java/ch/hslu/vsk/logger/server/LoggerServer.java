package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.*;

public class LoggerServer {
    private final ZMQSocketHandler socketHandler;
    private final MessageManager messageManager;

    public LoggerServer(String address, MessageManager messageManager) {
        this.socketHandler = new ZMQSocketHandler(address);
        this.messageManager = messageManager;
    }

    public void start() {
        System.out.println("LoggerServer started");
        while (!Thread.currentThread().isInterrupted()) {
            String message = socketHandler.receive();
            System.out.println("Received: " + message);
            if(message.equals(ClientRequestCodes.HEARTBEAT.toString())){
                socketHandler.reply(ServerResponseCodes.ALIVE.toString());
                continue;
            }
            messageManager.save(message);
            socketHandler.reply(ServerResponseCodes.RECEIVED.toString());
        }
    }
}