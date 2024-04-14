package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.api.Logger;
import ch.hslu.vsk.logger.api.LoggerSetup;

import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

class LoggerSetupImpl implements LoggerSetup {

    private LogLevel minLogLevel;
    private final String source;
    private final Path fallbackFile;
    private final URI targetServerAddress;

    LoggerSetupImpl(LoggerSetupBuilderImpl builder) {
        this.minLogLevel = builder.getMinLogLevel();
        this.source = builder.getSource();
        this.fallbackFile = builder.getFallbackFilePath();
        this.targetServerAddress = builder.getTargetServerAddress();
    }

    @Override
    public Logger createLogger() {
        Publisher publisher = new Publisher(targetServerAddress);
        MessageManager messageManager = new MessageManager(publisher, fallbackFile);
        return new LoggerImpl(this, messageManager);
    }

    @Override
    public LogLevel getMinLogLevel() {
        return minLogLevel;
    }

    @Override
    public void setMinLogLevel(LogLevel logLevel) {
        if (Objects.isNull(logLevel)) {
            throw new IllegalStateException("LogLevel cannot be null");
        }
        this.minLogLevel = logLevel;
    }

    public String getSource() {
        return source;
    }
}
