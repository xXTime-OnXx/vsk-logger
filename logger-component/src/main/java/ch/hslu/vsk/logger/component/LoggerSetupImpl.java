package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.api.Logger;
import ch.hslu.vsk.logger.api.LoggerSetup;

import java.net.URI;
import java.nio.file.Path;

public class LoggerSetupImpl implements LoggerSetup {

    private LogLevel minLogLevel;
    private String source;
    private Path fallbackFile;
    private URI targetServerAddress;

    LoggerSetupImpl(LoggerSetupBuilderImpl builder) {
        this.minLogLevel = builder.minLogLevel;
        this.source = builder.source;
        this.fallbackFile = builder.fallbackFile;
        this.targetServerAddress = builder.targetServerAddress;
    }

    @Override
    public Logger createLogger() {
        return new LoggerImpl(minLogLevel, targetServerAddress);
    }

    @Override
    public LogLevel getMinLogLevel() {
        return minLogLevel;
    }

    @Override
    public void setMinLogLevel(LogLevel logLevel) {
        this.minLogLevel = logLevel;
    }

}
