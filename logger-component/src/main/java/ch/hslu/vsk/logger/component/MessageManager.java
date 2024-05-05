package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.common.JsonMapper;
import ch.hslu.vsk.logger.common.LogMessage;
import ch.hslu.vsk.logger.common.StringPersistorAdapter;
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
    private final StringPersistorAdapter persistor;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public MessageManager(LoggerClient loggerClient, Path fallbackFilePath) {
        this.loggerClient = loggerClient;
        this.persistor = new StringPersistorAdapter(fallbackFilePath);
    }

    public void save(LogMessage logMessage) {
        String logMessageJson = JsonMapper.toString(logMessage);
        try{
            loggerClient.sendLogMessage(logMessageJson);
        } catch (Exception e) {
            System.out.println("Failed to send Message: " + e.getMessage());
            persistor.save(Instant.now(), logMessage.toCSV());
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
                        LogMessage logMessage = LogMessage.fromCSV(messages.getFirst().getPayload());
                        loggerClient.sendLogMessage(JsonMapper.toString(logMessage));
                        System.out.println("Sent stored message" );

                        messages.removeFirst();
                    } catch (ConnectException e) {
                        System.out.println("Failed to send stored message" );
                        loggerClient.reconnect();
                        break;
                    } catch (Exception e)
                    {
                        System.out.println("THIS IS A EXTREMELY RARE EXCEPTION!!!" + e.getMessage());
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
