package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.ConfigFileReader;
import ch.hslu.vsk.stringpersistor.api.StringPersistor;
import ch.hslu.vsk.stringpersistor.impl.StringPersistorFactory;

import java.nio.file.Path;
import java.util.Properties;

public class Application {
    public static void main(String[] args) {
        Properties prop = ConfigFileReader.read(Path.of("app.config"));

        StringPersistor stringPersistor = StringPersistorFactory.create(Path.of(prop.getProperty("logFilePath")));
        MessageManager messageManager = new MessageManager(stringPersistor);
        LoggerServer loggerServer = new LoggerServer(prop.getProperty("url") + ":" + prop.getProperty("port"), messageManager);
        loggerServer.start();
    }
}
