package ch.hslu.vsk.logger.common;

import ch.hslu.vsk.logger.api.LogLevel;

import java.time.Instant;

public class CsvStorageFormatStrategy implements StorageFormatStrategy {

    // TODO: add receivedAt to format
    @Override
    public String format(LogMessage logMessage) {
        return String.format(
                "%s,%s,%s,%s",
                logMessage.getSource(),
                logMessage.getLogLevel(),
                logMessage.getCreatedAt(),
                logMessage.getMessage());
    }

    @Override
    public LogMessage toLogMessage(String input) {
        String[] parts = input.split(",");
        return new LogMessage(
                parts[0],
                LogLevel.valueOf(parts[1]),
                parts[3],
                Instant.parse(parts[2]));
    }
}
