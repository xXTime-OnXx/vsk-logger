package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoggerComponentIT {

    private static final String DOCKER_IMAGE_NAME = "tiimonschmid/logger-server:latest";
    private static final int CONTAINER_PORT = 5555;

    @Container
    private static GenericContainer loggerServer = new GenericContainer(DockerImageName.parse(DOCKER_IMAGE_NAME))
            .withExposedPorts(CONTAINER_PORT);

    @BeforeAll
    static void beforeAll() {
        loggerServer.start();
    }

    @AfterAll
    static void afterAll() {
        loggerServer.stop();
    }

    @Test
    void multipleLoggerComponentInstances() {
        Logger logger1 = createLogger("logger-1");
        Logger logger2 = createLogger("logger-2");

        logger1.debug("Test Message Debug");
        logger2.debug("Test Message Debug");
        logger1.info("Test Message Info");
        logger2.info("Test Message Info");

        final String logs = loggerServer.getLogs();
        assertTrue(logs.contains("logger-1"));
        assertFalse(logs.contains("Test Message Debug"));
        assertTrue(logs.contains("Test Message Info"));
    }

    private Logger createLogger(String source) {
        LoggerSetupBuilder loggerSetupBuilder = LoggerSetupBuilderFactory.create();
        LoggerSetup loggerSetup = loggerSetupBuilder
                .requires(LogLevel.Info)
                .from(source)
                .usesAsFallback(Path.of("/dev", "null"))
                .targetsServer(URI.create("tcp://localhost:" + loggerServer.getMappedPort(CONTAINER_PORT)))
                .build();
        return loggerSetup.createLogger();
    }

}
