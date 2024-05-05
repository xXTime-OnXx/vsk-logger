package ch.hslu.vsk.logger.common;

import ch.hslu.vsk.logger.api.LogLevel;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CsvStorageFormatStrategyTest {
    private final String source = "unittest";
    private final LogLevel logLevel = LogLevel.Error;
    private final String message = "messagetext";
    private final Instant timestamp = Instant.now();
    private final LogMessage logMessage = new LogMessage(source, logLevel, message, timestamp);
    
    @Test
    void test_format_success() {
        StorageFormatStrategy storageFormatStrategy = new CsvStorageFormatStrategy();

        String result = storageFormatStrategy.format(logMessage);

        assertEquals(String.format("%s,%s,%s,%s", source, logLevel, timestamp, message), result);
    }

    @Test
    void test_toLogMessage_success() {
        StorageFormatStrategy storageFormatStrategy = new CsvStorageFormatStrategy();

        String formattedMessage = String.format("%s,%s,%s,%s", source, logLevel, timestamp, message);

        LogMessage result = storageFormatStrategy.toLogMessage(formattedMessage);

        assertEquals(logMessage, result);
    }
}
