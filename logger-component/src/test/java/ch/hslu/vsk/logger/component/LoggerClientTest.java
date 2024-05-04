package ch.hslu.vsk.logger.component;
import ch.hslu.vsk.stringpersistor.api.StringPersistor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.net.URI;
import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class LoggerClientTest {

    @Mock
    private ZContext context;
    @Mock
    private ZMQ.Socket socket;
    private LoggerClient client;
    private URI dummyUri = URI.create("tcp://localhost:5555");
    private Path fallbackFilePath = Path.of("fallback.txt");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        when(context.createSocket(any())).thenReturn(socket);
        client = new LoggerClient(dummyUri, fallbackFilePath);
    }

    @Test
    void testSuccessfulSend() {
        // Assume the connection is active
        when(socket.send(anyString())).thenReturn(true);
        when(socket.recvStr()).thenReturn("OK");

        String response = client.sendLogMessage("Test message");
        assertEquals("OK", response);
    }

    @Test
    void testConnectionFailure() {
        // Simulate a connection failure
        when(socket.send(anyString())).thenThrow(new RuntimeException("Connection lost"));

        String response = client.sendLogMessage("Test message");
        assertEquals("Message stored locally due to connection failure", response);
    }

    @Test
    void testReconnectionAndMessageFlushing() {
        // Set up to simulate a reconnection
        when(socket.send(anyString())).thenThrow(new RuntimeException("Connection lost"))
                .thenReturn(true);
        when(socket.recvStr()).thenReturn("OK");

        // Simulate storing a message
        client.sendLogMessage("Test message");
        // Reconnect and flush messages
        client.reconnect();
        verify(socket, times(2)).send("Test message"); // Including the retry
    }
}
