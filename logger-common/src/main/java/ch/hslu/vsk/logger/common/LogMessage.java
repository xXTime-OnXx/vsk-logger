package ch.hslu.vsk.logger.common;

import ch.hslu.vsk.logger.api.LogLevel;

import java.time.Instant;

public class LogMessage {
    private String source;
    private LogLevel logLevel;
    private String message;
    private Instant timestamp;

    public LogMessage() {}

    public LogMessage(final String source, final LogLevel logLevel, final String message) {
        this.source = source;
        this.logLevel = logLevel;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public String getSource() {
        return source;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String toStringWithoutTimestamp() {
        return String.format("%s: [%s] '%s'", source, logLevel, message);
    }
}
