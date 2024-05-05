package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.JsonMapper;
import ch.hslu.vsk.logger.common.LogMessage;
import ch.hslu.vsk.stringpersistor.api.StringPersistor;

public class MessageManager {

    private final StringPersistor stringPersistor;

    public MessageManager(StringPersistor stringPersistor) {
        this.stringPersistor = stringPersistor;
    }

    public void save(String message) {
        LogMessage logMessage = JsonMapper.fromString(message, LogMessage.class);
        logMessage.received();
        System.out.println("Received message: " + logMessage.toStringWithoutCreatedAt());
        stringPersistor.save(logMessage.getCreatedAt(), logMessage.toStringWithoutCreatedAt());
    }
}
