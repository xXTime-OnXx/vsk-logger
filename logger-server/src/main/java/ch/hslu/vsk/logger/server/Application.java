package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.*;

import java.nio.file.Path;

public class Application {
    public static void main(String[] args) {
        Config config = ConfigReader.read(Path.of("app.config"));

        if (config == null) {
            throw new RuntimeException("LoggerServer cant be started because the config is null!");
        }

        StorageFormatStrategy storageFormatStrategy = new FinalCompetitionStorageFormatStrategy();
        LogMessagePersistorImpl logMessagePersistor = new LogMessagePersistorImpl(config.getLogFilePath(), storageFormatStrategy);
        MessageManager messageManager = new MessageManager(logMessagePersistor);

        LoggerServer loggerServer = new LoggerServer(
                config.getUrl() + ":" + config.getPort(),
                messageManager);

        loggerServer.start();
    }
}
