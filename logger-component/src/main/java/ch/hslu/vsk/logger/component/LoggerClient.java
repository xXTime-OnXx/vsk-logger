package ch.hslu.vsk.logger.component;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.net.URI;

public class LoggerClient {
    private final ZContext context = new ZContext();
    private ZMQ.Socket socket;
    public LoggerClient(URI address) {
        socket = context.createSocket(SocketType.REQ);
        socket.connect(address.toString());
    }

    public String sendLogMessage(String logMessage) {
        socket.send(logMessage);
        return socket.recvStr();
    }
}
