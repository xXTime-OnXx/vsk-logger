package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.api.LoggerSetup;
import ch.hslu.vsk.logger.api.LoggerSetupBuilder;

import java.net.URI;
import java.nio.file.Path;

public class LoggerSetupBuilderImpl implements LoggerSetupBuilder {
    public LogLevel minLogLevel;
    public String source;
    public Path fallbackFile;
    public URI targetServerAddress;

    @Override
    public LoggerSetupBuilder requires(LogLevel minLogLevel) {
        this.minLogLevel = minLogLevel;
        return this;
    }

    @Override
    public LoggerSetupBuilder from(String source) {
        this.source = source;
        return this;
    }

    @Override
    public LoggerSetupBuilder usesAsFallback(Path path) {
        this.fallbackFile = path;
        return this;
    }

    @Override
    public LoggerSetupBuilder targetsServer(URI address) {
        this.targetServerAddress = address;
        return this;
    }

    @Override
    public LoggerSetup build() {
        // TODO: validation
        return new LoggerSetupImpl(this);
    }

}
