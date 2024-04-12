package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.stringpersistor.api.StringPersistor;
import ch.hslu.vsk.stringpersistor.impl.StringPersistorFactory;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.nio.file.Path;

public class Application {
    public static void main(String[] args) throws JsonProcessingException {
        StringPersistor stringPersistor = StringPersistorFactory.create(Path.of("log.txt"));
        LoggerServer loggerServer = new LoggerServer("tcp://localhost:5555", stringPersistor);
        loggerServer.start();
    }
}
