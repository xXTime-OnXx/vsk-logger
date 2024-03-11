package ch.hslu.vsk.logger.server;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

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

    public static void main(String[] args) {
        Subscriber subscriber = new Subscriber("tcp://localhost:5555");
        while (!Thread.currentThread().isInterrupted()) {
            String message = subscriber.receive();
            System.out.println(message);
        }
        subscriber.close();
    }

}