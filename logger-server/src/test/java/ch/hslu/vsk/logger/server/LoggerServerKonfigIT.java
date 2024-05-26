package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.ConfigFileReader;
import ch.hslu.vsk.logger.common.CsvStorageFormatStrategy;
import ch.hslu.vsk.logger.common.LogMessagePersistorImpl;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class LoggerServerKonfigIT {


    @TempDir
    static Path tempDir;


    @Test
    void testLoggerServerWithDifferentConfigurations() throws IOException, InterruptedException {
        int defaultPort = findAvailablePort();
        Path defaultConfig = createConfigFile("localhost", defaultPort, tempDir.resolve("default.txt"));
        startAndTestLoggerServer(defaultConfig, defaultPort);

        int customPort = findAvailablePort();
        Path customConfig = createConfigFile("127.0.0.1", customPort, tempDir.resolve("custom.txt"));
        startAndTestLoggerServer(customConfig, customPort);
    }

    private int findAvailablePort() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        }
    }

    private Path createConfigFile(String url, int port, Path logFilePath) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("url", "tcp://" + url);
        properties.setProperty("port", String.valueOf(port));
        properties.setProperty("logFilePath", logFilePath.toString());

        Path configFilePath = tempDir.resolve("app.config");
        try (FileWriter writer = new FileWriter(configFilePath.toFile())) {
            properties.store(writer, null);
        }
        return configFilePath;
    }

    private void startAndTestLoggerServer(Path configFilePath, int port) throws InterruptedException {
        Properties prop = ConfigFileReader.read(configFilePath);
        MessageManager messageManager = new MessageManager(new LogMessagePersistorImpl(Path.of(prop.getProperty("logFilePath")), new CsvStorageFormatStrategy()));

        LoggerServer loggerServerInstance = new LoggerServer(prop.getProperty("url") + ":" + prop.getProperty("port"), messageManager);
        System.out.println("Starting LoggerServer with config file: " + configFilePath + " on url: " + prop.getProperty("url") + " and port: " + port + "and log file path: " + prop.getProperty("logFilePath"));

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> future = executorService.submit(loggerServerInstance::start);

        // Simulate some waiting period to let the server start
        TimeUnit.SECONDS.sleep(1);

        // Interrupt the logger server thread
        future.cancel(true);

        // Ensure the thread is stopped
        executorService.shutdownNow();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("LoggerServer stopped successfully with config file: " + configFilePath);
    }
}
