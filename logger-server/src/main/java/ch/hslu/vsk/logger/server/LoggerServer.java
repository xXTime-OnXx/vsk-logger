package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.LogMessage;
import ch.hslu.vsk.stringpersistor.api.StringPersistor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class LoggerServer {
    private final ZMQSocketHandler socketHandler;
    private final StringPersistor stringPersistor;
    private final ObjectMapper objectMapper;

    public LoggerServer(String address, StringPersistor stringPersistor) {
        this.socketHandler = new ZMQSocketHandler(address);
        this.stringPersistor = stringPersistor;
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public void start() throws JsonProcessingException {
        while (!Thread.currentThread().isInterrupted()) {
            String message = socketHandler.receive();
            System.out.println("Received message: " + message);
            LogMessage logMessage = objectMapper.readValue(message, LogMessage.class);
            stringPersistor.save(logMessage.getTimestamp(), logMessage.getMessage());
            socketHandler.reply("received");
        }
    }

    public void stop() {
        socketHandler.close();
    }
}