package ch.hslu.vsk.logger.common;

import ch.hslu.vsk.logger.api.LogLevel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LogMessageTest {
    @Test
    void test_equals_success() {
        LogMessage logMessage = new LogMessage("unittest", LogLevel.Warning, "message");

        assertEquals(logMessage, logMessage);
    }

    @Test
    void test_equals_notEqual() {
        LogMessage logMessage = new LogMessage("unittest", LogLevel.Warning, "message");
        LogMessage logMessage2 = new LogMessage("unittest2", LogLevel.Warning, "message");

        assertNotEquals(logMessage, logMessage2);
    }
}
