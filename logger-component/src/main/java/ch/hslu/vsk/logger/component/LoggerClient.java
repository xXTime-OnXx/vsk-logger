package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.common.StringPersistorAdapter;
import ch.hslu.vsk.stringpersistor.api.PersistedString;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import javax.sound.midi.SysexMessage;
import java.net.URI;
import java.nio.file.Path;
import java.rmi.ConnectException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class LoggerClient {
    private final ZContext context = new ZContext();
    private ZMQ.Socket socket;
    private URI address;
    private final StringPersistorAdapter persistor;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public LoggerClient(URI address, Path fallbackFilePath) {
        this.address = address;
        this.persistor = new StringPersistorAdapter(fallbackFilePath);

        reconnect(); // Initial connection setup
    }

    public void reconnect() {

        if (socket != null) {
            context.destroySocket(socket);
        }
        socket = context.createSocket(SocketType.REQ);
        socket.connect(address.toString());
        socket.setReceiveTimeOut(1000);
    }

    public boolean testConnection() {
        try {
            socket.send("HEARTBEAT");
            String response = socket.recvStr();
            if (response != null && response.equals("ALIVE")) {
                System.out.println("Connection is still alive");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Reconnecting due to exception: " + e.getMessage());
        }
        return false;
    }
    public void sendLogMessage(String logMessage) throws ConnectException {
        System.out.println("Sending message: " + logMessage);
        socket.send(logMessage);
        System.out.println("Sent message: " + logMessage);

        String response = socket.recvStr();
        System.out.println("Received response: " + response);

        if (response == null || !response.equals("RECEIVED")) {
            System.out.println("Failed to send message: " + logMessage);
            throw new ConnectException("Message stored locally due to connection failure");
        }
    }
}