package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.JsonMapper;
import ch.hslu.vsk.logger.common.LogMessage;
import ch.hslu.vsk.logger.common.StringPersistorAdapter;
import ch.hslu.vsk.stringpersistor.api.StringPersistor;

public class LoggerServer {
    private final ZMQSocketHandler socketHandler;
    private StringPersistorAdapter stringPersistorAdapter;

    public LoggerServer(String address, StringPersistorAdapter stringPersistorAdapter) {
        this.socketHandler = new ZMQSocketHandler(address);
        this.stringPersistorAdapter = stringPersistorAdapter;
    }

    public void start() {
        while (!Thread.currentThread().isInterrupted()) {
            String message = socketHandler.receive();
            if(message.equals("HEARTBEAT")){
                System.out.println("Received heartbeat");
                socketHandler.reply("ALIVE");
                continue;
            }
            System.out.println("Received: " + message);
            LogMessage logMessage = JsonMapper.fromString(message, LogMessage.class);
            stringPersistorAdapter.save(logMessage.getTimestamp(), logMessage.toStringWithoutTimestamp());
            socketHandler.reply("RECEIVED");
            System.out.println("Received: " + logMessage.toString());
        }
    }
}