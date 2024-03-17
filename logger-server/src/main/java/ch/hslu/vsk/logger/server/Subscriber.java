package ch.hslu.vsk.logger.server;

import ch.hslu.vsk.logger.common.LogMessage;
import ch.hslu.vsk.stringpersistor.api.StringPersistor;
import ch.hslu.vsk.stringpersistor.impl.StringPersistorFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.nio.file.Path;

public class Subscriber {

    private final ZContext context = new ZContext();
    private final ZMQ.Socket socket = context.createSocket(SocketType.SUB);

    public Subscriber(String address) {
        if (!socket.connect(address)) {
            throw new RuntimeException("Cannot connect to " + address);
        }
        socket.subscribe("");
    }

    public String receive() {

        byte[] message = socket.recv();
        if (message == null) {
            throw new RuntimeException("failed to receive message (errno: " + socket.errno());
        }
        return new String(message, ZMQ.CHARSET);
    }

    public void close() {
        socket.close();
        context.close();
    }

    public static void main(String[] args) throws JsonProcessingException {
        Subscriber subscriber = new Subscriber("tcp://localhost:5555");
        StringPersistor persistor = StringPersistorFactory.create(Path.of("log.txt"));
        while (!Thread.currentThread().isInterrupted()) {
            String message = subscriber.receive();
            ObjectMapper objectMapper = new ObjectMapper();
            LogMessage logMessage = objectMapper.readValue(message, LogMessage.class);
            persistor.save(logMessage.getTimestamp(), logMessage.getMessage());
        }
        subscriber.close();
    }

}