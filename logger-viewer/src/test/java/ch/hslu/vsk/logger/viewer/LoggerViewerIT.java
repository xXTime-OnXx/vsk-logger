package ch.hslu.vsk.logger.viewer;

import ch.hslu.vsk.logger.api.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

class LoggerViewerIT {

    private static final String DOCKER_IMAGE_NAME = "tiimonschmid/logger-server:latest";
    private static final int LOG_MESSAGE_SOCKET_PORT = 5555;
    private static final int LOG_MESSAGE_VIEWER_HC_PORT = 5701;

    @Container
    private static GenericContainer loggerServer = new GenericContainer(DockerImageName.parse(DOCKER_IMAGE_NAME))
            .withExposedPorts(LOG_MESSAGE_SOCKET_PORT, LOG_MESSAGE_VIEWER_HC_PORT);

    @BeforeAll
    static void beforeAll() {
        loggerServer.start();
    }

    @AfterAll
    static void afterAll() {
        loggerServer.stop();
    }

    @Test
    void multipleLoggerComponentInstances() throws InterruptedException {
        CompletableFuture.runAsync(() -> Application.main(new String[]{}));
        Thread.sleep(3000);
        Logger logger1 = createLogger("logger-1");

        logger1.debug("Test Message Debug");
        logger1.info("Test Message Info");
        logger1.warn("Test Message Warn");
        logger1.error("Test Message Error");
    }

    private Logger createLogger(String source) {
        LoggerSetupBuilder loggerSetupBuilder = LoggerSetupBuilderFactory.create();
        LoggerSetup loggerSetup = loggerSetupBuilder
                .requires(LogLevel.Info)
                .from(source)
                .usesAsFallback(Path.of("/dev", "null"))
                .targetsServer(URI.create("tcp://localhost:" + loggerServer.getMappedPort(LOG_MESSAGE_SOCKET_PORT)))
                .build();
        return loggerSetup.createLogger();
    }

}