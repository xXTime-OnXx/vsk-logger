package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.common.JsonMapper;
import ch.hslu.vsk.logger.common.LogMessage;

import java.net.URI;

public class MessageManager {

    private final Publisher publisher;

    public MessageManager(URI targetServerAddress) {
        this.publisher = new Publisher(targetServerAddress);
    }

    public void save(LogMessage logMessage) {
        String logMessageJson = JsonMapper.toString(logMessage);
        publisher.send(logMessageJson);
    }
}
