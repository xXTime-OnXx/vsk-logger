package ch.hslu.vsk.logger.common;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigReader {
    public static Config read(Path configFilePath) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath.toString())) {
            prop.load(fis);
        }
        catch (Exception e) {
            System.out.println("Config file '" + configFilePath + "' couldn't be loaded: " + e.getMessage());
            return null;
        }

        boolean useConfigValues = Boolean.parseBoolean(prop.getProperty("useConfigValues"));

        return new Config(
                prop.getProperty("url"),
                Integer.parseInt(useConfigValues ? prop.getProperty("port") : System.getenv("LISTEN_PORT")),
                Path.of(useConfigValues ? prop.getProperty("logFilePath") : System.getenv("LOG_FILE")));
    }
}
