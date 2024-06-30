package ch.hslu.vsk.logger.viewer;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spi.exception.TargetDisconnectedException;

import java.io.InputStream;
import java.util.logging.LogManager;

public class Consumer {

    private final HazelcastInstance hazelcastInstance;
    private final IQueue<String> queue;
    private final LogMessageViewer logMessageViewer = new LogMessageViewer();

    public Consumer(String address) {
        setHazelcastLogLevel();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig().addAddress(address);
        hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        queue = hazelcastInstance.getQueue("log-message");
    }

    public String getMessage() throws LoggerServerDisconnectedException {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (TargetDisconnectedException e) {
            throw new LoggerServerDisconnectedException("LoggerServer is disconnected");
        }
    }

    public void serve() {
        removeOldLogMessages();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String message = getMessage();
                logMessageViewer.showLogMessage(message);
            } catch (LoggerServerDisconnectedException e) {
                System.out.println("LoggerServer is disconnected");
            }
        }
        hazelcastInstance.shutdown();
    }

    private void removeOldLogMessages() {
        queue.clear();
    }

    private void setHazelcastLogLevel() {
        try (InputStream stream = Consumer.class.getClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (Exception e) {
            throw new IllegalStateException("Couldn't set Hazelcast LogLevel to Warning", e);
        }
    }

}
