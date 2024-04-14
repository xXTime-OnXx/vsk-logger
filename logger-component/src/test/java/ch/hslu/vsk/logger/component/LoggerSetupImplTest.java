package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.api.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LoggerSetupImplTest {

    private LoggerSetupBuilderImpl loggerSetupBuilder;
    private LoggerSetupImpl loggerSetup;

    @BeforeEach
    void setUp() {
        loggerSetupBuilder = new LoggerSetupBuilderImpl();
        loggerSetupBuilder
                .requires(LogLevel.Info)
                .from("test-app")
                .usesAsFallback(Path.of("/dev", "null"))
                .targetsServer(URI.create("tcp://localhost:5555"));

        loggerSetup = new LoggerSetupImpl(loggerSetupBuilder);
    }

    @Test
    void constructorShouldReturnLoggerSetup() {
        assertEquals(loggerSetupBuilder.getMinLogLevel(), loggerSetup.getMinLogLevel());
        assertEquals(loggerSetupBuilder.getSource(), loggerSetup.getSource());
    }

    @Test
    void setMinLogLevelShouldSetMinLogLevel() {
        LogLevel minLogLevel = LogLevel.Error;

        loggerSetup.setMinLogLevel(minLogLevel);

        assertEquals(minLogLevel, loggerSetup.getMinLogLevel());
    }

    @Test
    void setMinLogLevelShouldThrowException() {
        assertThrows(IllegalStateException.class, () -> loggerSetup.setMinLogLevel(null));
    }

    @Test
    void createLoggerShouldReturnLogger() {
        Logger logger = loggerSetup.createLogger();

        assertNotNull(logger);
    }


}