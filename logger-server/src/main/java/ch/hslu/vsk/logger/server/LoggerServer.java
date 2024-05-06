package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.*;

public class LoggerServer {
    private final ZMQSocketHandler socketHandler;
    private final MessageManager messageManager;
    private final StorageFormatStrategy storageFormatStrategy;

    public LoggerServer(String address, StorageFormatStrategy storageFormatStrategy, MessageManager messageManager) {
        this.socketHandler = new ZMQSocketHandler(address);
        this.storageFormatStrategy = storageFormatStrategy;
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

            LogMessage logMessage = JsonMapper.fromString(message, LogMessage.class);

            String formattedLogMessage = storageFormatStrategy.format(logMessage);

            stringPersistorAdapter.save(logMessage.getTimestamp(), formattedLogMessage);
            messageManager.save(message);
            socketHandler.reply(ServerResponseCodes.RECEIVED.toString());
        }
    }
}