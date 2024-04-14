package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.common.JsonMapper;
import ch.hslu.vsk.logger.common.LogMessage;

import java.nio.file.Path;

class MessageManager {

    private final Publisher publisher;

    public MessageManager(Publisher publisher, Path fallbackFile) {
        this.publisher = publisher;
    }

    public void save(LogMessage logMessage) {
        String logMessageJson = JsonMapper.toString(logMessage);
        publisher.send(logMessageJson);
    }
}
