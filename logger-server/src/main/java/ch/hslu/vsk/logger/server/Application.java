package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.stringpersistor.api.StringPersistor;
import ch.hslu.vsk.stringpersistor.impl.StringPersistorFactory;

import java.nio.file.Path;

public class Application {
    public static void main(String[] args) {
        StringPersistor stringPersistor = StringPersistorFactory.create(Path.of("log.txt"));
        LoggerServer loggerServer = new LoggerServer("tcp://*:5555", stringPersistor);
        loggerServer.start();
    }
}
