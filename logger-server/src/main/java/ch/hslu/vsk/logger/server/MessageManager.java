package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.JsonMapper;
import ch.hslu.vsk.logger.common.LogMessage;
import ch.hslu.vsk.logger.common.LogMessagePersistor;

public class MessageManager {

    private final LogMessagePersistor logMessagePersistor;
    private final Producer producer;

    public MessageManager(LogMessagePersistor logMessagePersistor) {
        this.logMessagePersistor = logMessagePersistor;
        this.producer = new Producer();
    }

    public void save(String message) {
        LogMessage logMessage = JsonMapper.fromString(message, LogMessage.class);
        logMessage.received();
        System.out.println("Received message: " + logMessage.toStringWithoutCreatedAt());
        System.out.println("Received at: " + logMessage.getCreatedAt());
        System.out.println("Message: " + logMessage.getMessage());
        logMessagePersistor.save(logMessage);
        producer.send(message);
    }
}
