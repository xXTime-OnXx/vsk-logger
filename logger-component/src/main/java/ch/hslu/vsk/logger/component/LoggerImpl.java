package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.api.Logger;
import ch.hslu.vsk.logger.common.LogMessage;

import java.net.URI;

class LoggerImpl implements Logger {

    private final MessageManager messageManager;

    private final LogLevel minLogLevel;

    public LoggerImpl(LogLevel minLogLevel, URI targetServerAddress) {
        this.minLogLevel = minLogLevel;
        this.messageManager = new MessageManager(targetServerAddress);
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
        messageManager.save(logMessage);
    }

}
