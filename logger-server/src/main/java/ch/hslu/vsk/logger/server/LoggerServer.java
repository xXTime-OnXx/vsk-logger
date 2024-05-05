package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.*;

public class LoggerServer {
    private final ZMQSocketHandler socketHandler;
    private final StorageFormatStrategy storageFormatStrategy;
    private final StringPersistorAdapter stringPersistorAdapter;

    public LoggerServer(String address, StorageFormatStrategy storageFormatStrategy, StringPersistorAdapter stringPersistorAdapter) {
        this.socketHandler = new ZMQSocketHandler(address);
        this.storageFormatStrategy = storageFormatStrategy;
        this.stringPersistorAdapter = stringPersistorAdapter;
    }

    public void start() {
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
            socketHandler.reply(ServerResponseCodes.RECEIVED.toString());
        }
    }
}