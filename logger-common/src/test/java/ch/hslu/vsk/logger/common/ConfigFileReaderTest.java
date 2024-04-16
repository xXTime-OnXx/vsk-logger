package ch.hslu.vsk.logger.common;

import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigFileReaderTest {
    @Test
    public void testRead() throws IOException {
        Path tempFile = Files.createTempFile("test", ".config");
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("key1=value1\n");
            writer.write("key2=value2\n");
        }

        Properties properties = ConfigFileReader.Read(tempFile);

        assertEquals("value1", properties.getProperty("key1"));
        assertEquals("value2", properties.getProperty("key2"));
    }

    @Test
    public void testReadNonExistentFile() {
        Properties properties = ConfigFileReader.Read(Path.of("non-existent-file.properties"));

        assertTrue(properties.isEmpty());
    }
}
