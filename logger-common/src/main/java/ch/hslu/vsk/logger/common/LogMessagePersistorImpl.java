package ch.hslu.vsk.logger.common;

import ch.hslu.vsk.stringpersistor.api.StringPersistor;
import ch.hslu.vsk.stringpersistor.impl.StringPersistorFactory;

import java.nio.file.Path;
import java.util.List;

public class LogMessagePersistorImpl implements LogMessagePersistor{
    private final StringPersistor stringPersistor;
    private final StorageFormatStrategy formatStrategy;

    public LogMessagePersistorImpl(Path path, StorageFormatStrategy formatStrategy) {
        this.stringPersistor = StringPersistorFactory.create(path);
        this.formatStrategy = formatStrategy;
    }

    public void save(LogMessage message) {
        System.out.println("Saving message: " + message);
        System.out.println("Saving message: " + message.getCreatedAt());
        System.out.println("Saving message: " + formatStrategy.format(message));
        stringPersistor.save(message.getCreatedAt(), formatStrategy.format(message));
    }

    public List<LogMessage> get(int count) {
        return stringPersistor.get(count).stream()
                .map(persistedString -> formatStrategy.toLogMessage(persistedString.getPayload()))
                .toList();
    }

}
