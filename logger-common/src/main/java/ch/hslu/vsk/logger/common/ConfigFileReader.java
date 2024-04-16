package ch.hslu.vsk.logger.common;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigFileReader {
    public static Properties Read(Path configFilePath) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(configFilePath.toString())) {
            prop.load(fis);
        }
        catch (Exception e) {
            System.out.println("Config file '" + configFilePath + "' couldn't be loaded: " + e.getMessage());
        }

        return prop;
    }
}
