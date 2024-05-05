package ch.hslu.vsk.logger.common;

import ch.hslu.vsk.logger.api.LogLevel;

import java.time.Instant;

public class LogMessage {
    private String source;
    private LogLevel logLevel;
    private String message;
    private Instant createdAt;
    private Instant receivedAt;

    public LogMessage() {}

    public LogMessage(final String source, final LogLevel logLevel, final String message) {
        this.source = source;
        this.logLevel = logLevel;
        this.message = message;
        this.createdAt = Instant.now();
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public void received() {
        this.receivedAt = Instant.now();
    }

    public String toStringWithoutCreatedAt() {
        return String.format("[%s] %s: [%s] '%s'", receivedAt, source, logLevel, message);
    }
}
