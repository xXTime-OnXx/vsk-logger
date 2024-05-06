package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.common.LogMessage;
import ch.hslu.vsk.logger.common.StorageFormatStrategy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.rmi.ConnectException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageManagerTest {

    @Mock
    LoggerClient loggerClient;

    @Mock
    StorageFormatStrategy storageFormatStrategy;

    @Test
    void saveShouldSendLogMessageAsJsonToTarget() throws JsonProcessingException, ConnectException {
        LogMessage logMessage = new LogMessage("Test",LogLevel.Info,"Log Message");

        MessageManager messageManager = new MessageManager(loggerClient, storageFormatStrategy, Path.of("test.txt"));
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String logMessageJson = objectMapper.writeValueAsString(logMessage);

        messageManager.save(logMessage);

        verify(loggerClient).sendLogMessage(logMessageJson);
    }
}