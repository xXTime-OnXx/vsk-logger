package ch.hslu.vsk.logger.server;

public class LoggerServer {
    private final ZMQSocketHandler socketHandler;
    private final MessageManager messageManager;

    public LoggerServer(String address, MessageManager messageManager) {
        this.socketHandler = new ZMQSocketHandler(address);
        this.messageManager = messageManager;
    }

    public void start() {
        System.out.println("LoggerServer started");
        while (!Thread.currentThread().isInterrupted()) {
            String message = socketHandler.receive();
            messageManager.save(message);
            socketHandler.reply("received");
        }
    }
}