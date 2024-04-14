package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.api.LoggerSetup;
import ch.hslu.vsk.logger.api.LoggerSetupBuilder;

import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

public class LoggerSetupBuilderImpl implements LoggerSetupBuilder {

    private LogLevel minLogLevel;
    private String source;
    private Path fallbackFilePath;
    private URI targetServerAddress;

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
        this.fallbackFilePath = path;
        return this;
    }

    @Override
    public LoggerSetupBuilder targetsServer(URI address) {
        this.targetServerAddress = address;
        return this;
    }

    @Override
    public LoggerSetup build() {
        if (Objects.isNull(minLogLevel)) {
            throw new IllegalStateException("LogLevel cannot be null");
        }
        if (Objects.isNull(source) || source.isEmpty()) {
            throw new IllegalStateException("Source cannot be null or empty");
        }
        if (Objects.isNull(fallbackFilePath)) {
            throw new IllegalStateException("Fallback path cannot be null");
        }
        if (Objects.isNull(targetServerAddress)) {
            throw new IllegalStateException("Target server address cannot be null");
        }
        return new LoggerSetupImpl(this);
    }

    public LogLevel getMinLogLevel() {
        return minLogLevel;
    }

    public String getSource() {
        return source;
    }

    public Path getFallbackFilePath() {
        return fallbackFilePath;
    }

    public URI getTargetServerAddress() {
        return targetServerAddress;
    }

}
