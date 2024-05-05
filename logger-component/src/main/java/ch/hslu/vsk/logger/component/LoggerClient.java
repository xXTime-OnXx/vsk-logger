package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.common.StringPersistorAdapter;
import ch.hslu.vsk.stringpersistor.api.PersistedString;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

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
        startReconnectionTask();
    }

    public void reconnect() {
        if (socket != null) {
            context.destroySocket(socket);
        }
        socket = context.createSocket(SocketType.REQ);
        socket.connect(address.toString());
    }

    private void startReconnectionTask() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Test sending a heartbeat or similar
                socket.send("HEARTBEAT");
                if (socket.recvStr() == null) { // Wenn keine Antwort empfangen wird
                    reconnect();
                }else{
                    sendStoredMessages();
                }
            } catch (Exception e) {
                reconnect();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public void sendStoredMessages() {
        List<PersistedString> messages = persistor.get(Integer.MAX_VALUE);
        for (PersistedString ps : messages) {
            sendLogMessage(ps.getPayload());
        }
    }

    public String sendLogMessage(String logMessage) {
        try {
            socket.send(logMessage);
            String response = socket.recvStr();
            if (response != null) {
                return response;
            } else {
                throw new ConnectException("Message stored locally due to connection failure");
            }
        } catch (Exception e) {
            persistor.save(Instant.now(), logMessage);
            return "Message stored locally due to connection failure"; //Hier könnte auch eine Exception gewordfen werden, wenn man eine ExceptionMiddleWare baut, welche sich darum kümmert.
        }
    }
}