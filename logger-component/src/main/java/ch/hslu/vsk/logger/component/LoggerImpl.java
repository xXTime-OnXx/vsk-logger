package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.api.Logger;
import ch.hslu.vsk.logger.common.LogMessage;

class LoggerImpl implements Logger {

    private final MessageManager messageManager;
    private final LoggerSetupImpl loggerSetup;

    public LoggerImpl(LoggerSetupImpl loggerSetup, MessageManager messageManager) {
        this.loggerSetup = loggerSetup;
        this.messageManager = messageManager;
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
        if (logLevel.ordinal() <= loggerSetup.getMinLogLevel().ordinal()) {
            LogMessage logMessage = new LogMessage(loggerSetup.getSource(), logLevel, message);
            messageManager.save(logMessage);
        }
    }

}
