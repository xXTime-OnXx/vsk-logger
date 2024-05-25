package ch.hslu.vsk.logger.common;

import ch.hslu.vsk.logger.api.LogLevel;

import java.time.Instant;
import java.util.Objects;

public class LogMessage {
    private String source;
    private LogLevel logLevel;
    private String message;
    private Instant createdAt;
    private Instant receivedAt;

    /**
     * Only for use for JsonMapper mapping.
     */
    protected LogMessage() {}

    public LogMessage(final String source, final LogLevel logLevel, final String message) {
        this(source, logLevel, message, Instant.now());
    }

    public LogMessage(final String source, final LogLevel logLevel, final String message, final Instant timestamp) {
        this.source = source;
        this.logLevel = logLevel;
        this.message = message;
        this.createdAt = timestamp;
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

    @Override
    public boolean equals(Object object) {
        if (this.getClass() != object.getClass()) {
            return false;
        }
        LogMessage logMessage = (LogMessage) object;
        return this.source.equals(logMessage.source)
                && this.message.equals(logMessage.message)
                && Objects.equals(this.logLevel, logMessage.logLevel)
                && this.createdAt.equals(logMessage.createdAt);
    }

    public String toStringWithoutCreatedAt() {
        return String.format("[%s] %s: [%s] '%s'", receivedAt, source, logLevel, message);
    }
}
