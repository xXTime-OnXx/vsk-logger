package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.ConfigFileReader;
import ch.hslu.vsk.logger.common.CsvStorageFormatStrategy;
import ch.hslu.vsk.logger.common.StorageFormatStrategy;
import ch.hslu.vsk.logger.common.StringPersistorAdapter;

import java.nio.file.Path;
import java.util.Properties;

public class Application {
    public static void main(String[] args) {
        Properties prop = ConfigFileReader.read(Path.of("app.config"));

        StringPersistorAdapter stringPersistorAdapter = new StringPersistorAdapter(Path.of(prop.getProperty("logFilePath")));
        StorageFormatStrategy storageFormatStrategy = new CsvStorageFormatStrategy();
        MessageManager messageManager = new MessageManager(stringPersistorAdapter, storageFormatStrategy);

        LoggerServer loggerServer = new LoggerServer(
                prop.getProperty("url") + ":" + prop.getProperty("port"),
                messageManager);

        loggerServer.start();
    }
}
