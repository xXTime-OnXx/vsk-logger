package ch.hslu.vsk.logger.viewer;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Consumer {

    private static final ZContext context = new ZContext();
    private final ZMQ.Socket socket = context.createSocket(SocketType.PULL);

    public Consumer(String address) {
        if (!socket.connect(address)) {
            throw new RuntimeException("Cannot connect to " + address);
        }
    }

    public String getMessage() {
        byte[] message = socket.recv();
        if (message == null) {
            throw new RuntimeException("failed to receive message (errno: " + socket.errno() +")");
        }
        return new String(message, ZMQ.CHARSET);
    }

    public void serve() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Consumer startet");
            String message = getMessage();
            System.out.println("Message received");
            System.out.println(message);
        }
    }

    public static void main(String[] args)  {
        Consumer consumer = new Consumer("tcp://localhost:5555");
        consumer.serve();
    }

}
