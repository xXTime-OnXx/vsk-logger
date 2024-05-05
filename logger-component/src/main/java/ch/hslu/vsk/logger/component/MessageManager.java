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
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final StringPersistorAdapter persistor = new StringPersistorAdapter(Path.of("logMessages.txt"));
    public MessageManager(LoggerClient loggerClient) {
        this.loggerClient = loggerClient;
    }

    public void save(LogMessage logMessage) {
        String logMessageJson = JsonMapper.toString(logMessage);
        try{
            loggerClient.sendLogMessage(logMessageJson);
        } catch (Exception e) {
            System.out.println("Message stored locally due to connection failure");
            persistor.save(Instant.now(), logMessage.toCSV());
            scheduleReconnection();
        }
    }

        public void scheduleReconnection() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Reconnecting...");
            boolean isConnected = loggerClient.testConnection();
            if(isConnected){
                List<PersistedString> messages = persistor.get(Integer.MAX_VALUE);
                System.out.println("stored messages are of size: " + messages.size());

                while (!messages.isEmpty()) {
                    System.out.println("Stored message: " + messages.getFirst().getPayload());
                    try {
                        LogMessage logMessage = LogMessage.fromCSV(messages.getFirst().getPayload());
                        loggerClient.sendLogMessage(JsonMapper.toString(logMessage));
                        messages.removeFirst();

                    } catch (ConnectException e) {
                        System.out.println("Failed to send stored message" );
                        loggerClient.reconnect();
                        break;
                    }catch (Exception e)
                    {
                        System.out.println("THIS IS A EXTREMELY RARE EXCEPTION!!!" + e.getMessage());
                    }
                    System.out.println("Sent stored message" );
                    try {
                        System.out.println("stored messages before are of size: " + messages.size());
                        messages.addAll(persistor.get(Integer.MAX_VALUE));
                        System.out.println("stored messages are of size: " + messages.size());
                    } catch (Exception e) {
                        System.out.println("Failed to get stored messages" );
                        break;
                    }
                }

//                for (PersistedString ps : messages) {
//                    System.out.println("Stored message: " + ps.getPayload());
//                    try {
//                        System.out.println("Convert to csv" );
//
//                        LogMessage logMessage = LogMessage.fromCSV(ps.getPayload());
//                        System.out.println("Sending converted stored message"+ logMessage.toString());
//                        loggerClient.sendLogMessage(JsonMapper.toString(logMessage));
//                    } catch (ConnectException e) {
//                        System.out.println("Failed to send stored message" );
//                        loggerClient.reconnect();
//                    }catch (Exception e)
//                    {
//                        System.out.println("THIS IS A EXTREMELY RARE EXCEPTION!!!" + e.getMessage());
//                        e.printStackTrace();
//                    }
//                    System.out.println("Sent stored message" );
//                }
                scheduler.shutdown();

            }else {
                loggerClient.reconnect();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
}
