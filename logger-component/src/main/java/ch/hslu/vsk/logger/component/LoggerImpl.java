package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.api.Logger;
import ch.hslu.vsk.logger.common.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;

class LoggerImpl implements Logger {

    private final LogLevel minLogLevel;
    private final Publisher publisher;

    public LoggerImpl(LogLevel minLogLevel, URI targetServerAddress) {
        this.minLogLevel = minLogLevel;
        this.publisher = new Publisher(targetServerAddress);
    }

    @Override
    public void debug(String message) {
        log(LogLevel.Debug, message);
    }

    @Override
    public void info(String message) {
        log(LogLevel.Info, message);
    }

    @Override
    public void warn(String message) {
        log(LogLevel.Warning, message);
    }

    @Override
    public void error(String message) {
        log(LogLevel.Error, message);
    }

    @Override
    public void error(String message, Exception exception) {
        String logErrorMessage = String.format("%s: %s", message, exception.getMessage());
        log(LogLevel.Error, logErrorMessage);
    }

    @Override
    public void log(LogLevel logLevel, String message) {
        if (logLevel.ordinal() > minLogLevel.ordinal()) {
            return;
        }
        LogMessage logMessage = new LogMessage(String.format("[%s] %s", logLevel.name(), message));
        publisher.send(mapToJson(logMessage));
    }

    private static String mapToJson(LogMessage logMessage) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(logMessage);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Couldn't parse Object to JSON", e);
        }
    }

}
