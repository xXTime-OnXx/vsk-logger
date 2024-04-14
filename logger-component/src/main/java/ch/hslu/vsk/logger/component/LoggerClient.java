package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.common.LogMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class LoggerClient {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final ZContext context = new ZContext();
    private ZMQ.Socket socket;
    public LoggerClient(String address) {
        socket = context.createSocket(SocketType.REQ);
        socket.connect(address);
    }

    public String sendLogMessage(LogMessage logMessage) throws JsonProcessingException {
        String logMessageAsString = objectMapper.writeValueAsString(logMessage);
        socket.send(logMessageAsString);
        return socket.recvStr();
    }
}
