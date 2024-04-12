package ch.hslu.vsk.logger.server;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class ZMQSocketHandler {
    private static final ZContext context = new ZContext();
    private final ZMQ.Socket socket = context.createSocket(SocketType.REP);

    public ZMQSocketHandler(String address) {
        if (!socket.bind(address)) {
            throw new RuntimeException("Cannot bind to " + address);
        }
    }

    public String receive() {
        byte[] message = socket.recv();
        if (message == null) {
            throw new RuntimeException("Failed to receive message (errno: " + socket.errno());
        }
        return new String(message, ZMQ.CHARSET);
    }

    public void reply(String message) {
        boolean isSent = socket.send(message);
        if (!isSent) {
            throw new RuntimeException("Cannot send reply: " + message);
        }
    }
}
