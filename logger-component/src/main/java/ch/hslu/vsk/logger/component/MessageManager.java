package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.common.JsonMapper;
import ch.hslu.vsk.logger.common.LogMessage;

import java.nio.file.Path;

class MessageManager {

    private final LoggerClient loggerClient;

    public MessageManager(LoggerClient loggerClient, Path fallbackFile) {
        this.loggerClient = loggerClient;
    }

    public void save(LogMessage logMessage) {
        String logMessageJson = JsonMapper.toString(logMessage);
        String response = loggerClient.sendLogMessage(logMessageJson);
    }
}
