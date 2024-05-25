package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.*;

import java.nio.file.Path;
import java.util.Properties;

public class Application {
    public static void main(String[] args) {
        Properties prop = ConfigFileReader.read(Path.of("app.config"));
        StorageFormatStrategy storageFormatStrategy = new CsvStorageFormatStrategy();
        LogMessagePersistorImpl logMessagePersistor = new LogMessagePersistorImpl(Path.of(prop.getProperty("logFilePath")), storageFormatStrategy);
        MessageManager messageManager = new MessageManager(logMessagePersistor);

        LoggerServer loggerServer = new LoggerServer(
                prop.getProperty("url") + ":" + prop.getProperty("port"),
                messageManager);

        loggerServer.start();
    }
}
