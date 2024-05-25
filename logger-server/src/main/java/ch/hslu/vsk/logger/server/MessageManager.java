package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.JsonMapper;
import ch.hslu.vsk.logger.common.LogMessage;
import ch.hslu.vsk.logger.common.StorageFormatStrategy;
import ch.hslu.vsk.stringpersistor.api.StringPersistor;

public class MessageManager {

    private final StringPersistor stringPersistor;
    private final StorageFormatStrategy storageFormatStrategy;

    public MessageManager(StringPersistor stringPersistor, StorageFormatStrategy storageFormatStrategy) {
        this.stringPersistor = stringPersistor;
        this.storageFormatStrategy = storageFormatStrategy;
    }

    public void save(String message) {
        LogMessage logMessage = JsonMapper.fromString(message, LogMessage.class);
        logMessage.received();
        System.out.println("Received message: " + logMessage.toStringWithoutCreatedAt());
        String formattedLogMessage = storageFormatStrategy.format(logMessage);
        stringPersistor.save(logMessage.getCreatedAt(), formattedLogMessage);
    }
}
