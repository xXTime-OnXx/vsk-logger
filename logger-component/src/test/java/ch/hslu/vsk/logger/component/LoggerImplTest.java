package ch.hslu.vsk.logger.component;

import ch.hslu.vsk.logger.api.LogLevel;
import ch.hslu.vsk.logger.common.LogMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LoggerImplTest {

    private LoggerImpl logger;
    private LoggerSetupImpl loggerSetup;

    @Mock
    MessageManager messageManager;

    @Captor
    ArgumentCaptor<LogMessage> logMessageCaptor;

    @BeforeEach
    void setUp() {
        LoggerSetupBuilderImpl loggerSetupBuilder = new LoggerSetupBuilderImpl();
        loggerSetup = (LoggerSetupImpl) loggerSetupBuilder
                .requires(LogLevel.Info)
                .from("test-app")
                .usesAsFallback(Path.of("/dev", "null"))
                .targetsServer(URI.create("tcp://localhost:5555"))
                .build();
        logger = new LoggerImpl(loggerSetup, messageManager);
    }

    @Test
    void logShouldNotCallMessageManagerWhenBelowMinLogLevel() {
        loggerSetup.setMinLogLevel(LogLevel.Error);

        logger.log(LogLevel.Debug, "Debug Message");
        logger.log(LogLevel.Info, "Info Message");
        logger.log(LogLevel.Warning, "Warning Message");

        verify(messageManager, never()).save(any());
    }

    @Test
    void logShouldCallMessageManagerWithLogMessage() {
        logger.log(LogLevel.Info, "Log Message");

        verify(messageManager).save(logMessageCaptor.capture());
        LogMessage logMessage = logMessageCaptor.getValue();

        assertEquals("[Info] Log Message", logMessage.getMessage());
    }

    @Test
    void errorShouldCallLogWithErrorMessage() {
        loggerSetup.setMinLogLevel(LogLevel.Error);

        logger.error("Error Log Message", new IllegalStateException("Exception Message"));

        verify(messageManager).save(logMessageCaptor.capture());
        LogMessage logMessage = logMessageCaptor.getValue();
        assertEquals("[Error] Error Log Message: Exception Message", logMessage.getMessage());
    }

    @Test
    void debugShouldCallLogWithDebugLevel() {
        loggerSetup.setMinLogLevel(LogLevel.Debug);

        logger.debug("Debug Log Message");

        verify(messageManager).save(logMessageCaptor.capture());
        LogMessage logMessage = logMessageCaptor.getValue();
        assertEquals("[Debug] Debug Log Message", logMessage.getMessage());
    }

    @Test
    void infoShouldCallLogWithInfoLevel() {
        loggerSetup.setMinLogLevel(LogLevel.Info);

        logger.info("Info Log Message");

        verify(messageManager).save(logMessageCaptor.capture());
        LogMessage logMessage = logMessageCaptor.getValue();
        assertEquals("[Info] Info Log Message", logMessage.getMessage());
    }

    @Test
    void warnShouldCallLogWithWarningLevel() {
        loggerSetup.setMinLogLevel(LogLevel.Warning);

        logger.warn("Warning Log Message");

        verify(messageManager).save(logMessageCaptor.capture());
        LogMessage logMessage = logMessageCaptor.getValue();
        assertEquals("[Warning] Warning Log Message", logMessage.getMessage());
    }

    @Test
    void errorShouldCallLogWithErrorLevel() {
        loggerSetup.setMinLogLevel(LogLevel.Error);

        logger.error("Error Log Message");

        verify(messageManager).save(logMessageCaptor.capture());
        LogMessage logMessage = logMessageCaptor.getValue();
        assertEquals("[Error] Error Log Message", logMessage.getMessage());
    }

}