package ch.hslu.vsk.logger.common;

import java.time.Instant;

public class LogMessage {
    private String message;
    private Instant timestamp;

    public LogMessage() {}

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

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
