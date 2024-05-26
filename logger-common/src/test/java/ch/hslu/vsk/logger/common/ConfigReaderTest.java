package ch.hslu.vsk.logger.common;

import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigReaderTest {
    @Test
    public void testRead() throws IOException {
        String url = "tcp://localhost";
        int port = 5555;
        Path logFilePath = Path.of("test.txt");
        Path tempFile = Files.createTempFile("test", ".config");
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("useConfigValues=true\n");
            writer.write("url="+ url +"\n");
            writer.write("port="+ port +"\n");
            writer.write("logFilePath="+ logFilePath +"\n");
        }

        Config config = ConfigReader.read(tempFile);

        assertEquals(url, config.getUrl());
        assertEquals(port, config.getPort());
        assertEquals(logFilePath, config.getLogFilePath());
    }

    @Test
    public void testReadNonExistentFile() {
        Config config = ConfigReader.read(Path.of("non-existent-file.properties"));

        assertNull(config);
    }
}
