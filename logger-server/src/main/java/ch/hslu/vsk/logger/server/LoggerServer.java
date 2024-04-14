package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.JsonMapper;
import ch.hslu.vsk.logger.common.LogMessage;
import ch.hslu.vsk.stringpersistor.api.StringPersistor;

public class LoggerServer {
    private final ZMQSocketHandler socketHandler;
    private final StringPersistor stringPersistor;

    public LoggerServer(String address, StringPersistor stringPersistor) {
        this.socketHandler = new ZMQSocketHandler(address);
        this.stringPersistor = stringPersistor;
    }

    public void start() {
        while (!Thread.currentThread().isInterrupted()) {
            String message = socketHandler.receive();
            System.out.println("Received message: " + message);
            LogMessage logMessage = JsonMapper.fromString(message, LogMessage.class);
            stringPersistor.save(logMessage.getTimestamp(), logMessage.getMessage());
            socketHandler.reply("received");
        }
    }
}