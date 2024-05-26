package ch.hslu.vsk.logger.common;

import java.nio.file.Path;

public class Config {
    private String url;
    private int port;
    private Path logFilePath;

    public Config (final String url, final int port, final Path logFilePath) {
        this.url = url;
        this.port = port;
        this.logFilePath = logFilePath;
    }

    public String getUrl() {
        return this.url;
    }

    public int getPort() {
        return this.port;
    }

    public Path getLogFilePath() {
        return this.logFilePath;
    }
}
