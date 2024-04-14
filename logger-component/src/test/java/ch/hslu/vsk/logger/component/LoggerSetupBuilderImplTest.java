package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.api.LoggerSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class LoggerSetupBuilderImplTest {

    private LoggerSetupBuilderImpl loggerSetupBuilder;

    @BeforeEach
    void setUp() {
        loggerSetupBuilder = new LoggerSetupBuilderImpl();
    }

    @Test
    void buildShouldReturnLoggerSetup() {
        LoggerSetup loggerSetup = loggerSetupBuilder
                .requires(LogLevel.Info)
                .from("test-app")
                .usesAsFallback(Path.of("/dev", "null"))
                .targetsServer(URI.create("http://localhost:1234"))
                .build();

        assertEquals(LogLevel.Info, loggerSetupBuilder.getMinLogLevel());
        assertEquals("test-app", loggerSetupBuilder.getSource());
        assertEquals("\\dev\\null", loggerSetupBuilder.getFallbackFilePath().toString());
        assertEquals("http://localhost:1234", loggerSetupBuilder.getTargetServerAddress().toString());

        assertTrue(Objects.nonNull(loggerSetup));
        assertEquals(LogLevel.Info, loggerSetup.getMinLogLevel());
    }

    @Test
    void whenMinLogLevelNull_buildShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> loggerSetupBuilder
                .requires(null)
                .from("test-app")
                .usesAsFallback(Path.of("/dev", "null"))
                .targetsServer(URI.create("http://localhost:1234"))
                .build());
    }

    @Test
    void whenSourceNull_buildShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> loggerSetupBuilder
                .requires(LogLevel.Info)
                .from(null)
                .usesAsFallback(Path.of("/dev", "null"))
                .targetsServer(URI.create("http://localhost:1234"))
                .build());
    }

    @Test
    void whenSourceEmpty_buildShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> loggerSetupBuilder
                .requires(LogLevel.Info)
                .from("")
                .usesAsFallback(Path.of("/dev", "null"))
                .targetsServer(URI.create("http://localhost:1234"))
                .build());
    }

    @Test
    void whenFallbackFilePathNull_buildShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> loggerSetupBuilder
                .requires(LogLevel.Info)
                .from("test-app")
                .usesAsFallback(null)
                .targetsServer(URI.create("http://localhost:1234"))
                .build());
    }

    @Test
    void whenTargetServerAddressNull_buildShouldThrowIllegalStateException() {
        assertThrows(IllegalStateException.class, () -> loggerSetupBuilder
                .requires(LogLevel.Info)
                .from("test-app")
                .usesAsFallback(Path.of("/dev", "null"))
                .targetsServer(null)
                .build());
    }
}