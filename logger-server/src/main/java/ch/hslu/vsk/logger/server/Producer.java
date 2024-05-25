package ch.hslu.vsk.logger.server;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Producer {
    private static final ZContext context = new ZContext();
    private final ZMQ.Socket socket = context.createSocket(SocketType.PUSH);

    public Producer(String address) {
        if (!socket.bind(address)) {
            throw new RuntimeException("Cannot bind to " + address);
        }
    }

    public void send(String message) {
        // TODO: check if subscriber is available, otherwise dont send message
        if (!socket.send(message.getBytes(ZMQ.CHARSET))) {
            System.out.println("sending of message failed, aborting");
        }
    }

    public static void main(String[] args)  {
        Producer producer = new Producer("tcp://localhost:5555");
        int counter = 1;
        while (!Thread.currentThread().isInterrupted()) {
            producer.send("#" + counter + " producer message");
            counter++;
            sleep();
        }
    }

    public static void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
