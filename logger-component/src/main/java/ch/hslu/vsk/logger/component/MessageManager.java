package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.common.*;
import ch.hslu.vsk.stringpersistor.api.PersistedString;

import java.nio.file.Path;
import java.rmi.ConnectException;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class MessageManager {
    private final LoggerClient loggerClient;
    private final StorageFormatStrategy storageFormatStrategy;
    private final LogMessagePersistorImpl persistor;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public MessageManager(LoggerClient loggerClient, StorageFormatStrategy storageFormatStrategy, Path fallbackFilePath) {
        this.loggerClient = loggerClient;
        this.storageFormatStrategy = storageFormatStrategy;
        this.persistor = new LogMessagePersistorImpl(fallbackFilePath, storageFormatStrategy);
    }

    public void save(LogMessage logMessage) {
        String logMessageJson = JsonMapper.toString(logMessage);
        try{
            loggerClient.sendLogMessage(logMessageJson);
        } catch (Exception e) {
            System.out.println("Failed to send Message: " + e.getMessage());
            persistor.save(logMessage);
            System.out.println("Message stored locally due to connection failure");

            scheduleReconnection();
        }
    }

    public void scheduleReconnection() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Reconnecting...");
            boolean isConnected = loggerClient.testConnection();
            if(isConnected) {
                List<PersistedString> messages = persistor.get(Integer.MAX_VALUE);

                while (!messages.isEmpty()) {
                    try {
                        LogMessage logMessage = storageFormatStrategy.toLogMessage(messages.getFirst().getPayload());
                        loggerClient.sendLogMessage(JsonMapper.toString(logMessage));
                        System.out.println("Sent stored message" );

                        messages.removeFirst();
                    } catch (ConnectException e) {
                        System.out.println("Failed to send stored message" );
                        loggerClient.reconnect();
                        break;
                    }

                    messages.addAll(persistor.get(Integer.MAX_VALUE));
                }

                scheduler.shutdown();
            } else {
                loggerClient.reconnect();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
}
