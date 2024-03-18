package ch.hslu.vsk.logger.component;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.net.URI;

class Publisher implements AutoCloseable {

    private final ZContext context = new ZContext();
    private final ZMQ.Socket socket = context.createSocket(SocketType.PUB);

    public Publisher(URI address) {
        socket.bind(address.toString());
    }

    public void send(String message) {
        boolean sentSuccessful = socket.send(message.getBytes(ZMQ.CHARSET));
        if (!sentSuccessful) {
            throw new RuntimeException("failed to send message (errno: " + socket.errno());
        }
    }

    @Override
    public void close() {
        socket.close();
        context.close();
    }

}
