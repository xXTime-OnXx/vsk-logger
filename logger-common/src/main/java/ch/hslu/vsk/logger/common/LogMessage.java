package ch.hslu.vsk.logger.common;

import java.time.Instant;


public class LogMessage {
    private final String message;
    private final Instant timestamp;

    public LogMessage(final String message, final Instant timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }
    public LogMessage(final String message) {
        this.message = message;
        this.timestamp = Instant.now();
    }
    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }


}
