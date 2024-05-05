package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.common.LogMessage;
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

    @Test
    void saveShouldSendLogMessageAsJsonToTarget() throws JsonProcessingException, ConnectException {
        MessageManager messageManager = new MessageManager(loggerClient);
        LogMessage logMessage = new LogMessage("Test",LogLevel.Info,"Log Message");
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String logMessageJson = objectMapper.writeValueAsString(logMessage);

        messageManager.save(logMessage);

        verify(loggerClient).sendLogMessage(logMessageJson);
    }

}