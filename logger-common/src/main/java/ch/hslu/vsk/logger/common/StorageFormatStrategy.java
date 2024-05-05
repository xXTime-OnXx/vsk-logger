package ch.hslu.vsk.logger.common;

public interface StorageFormatStrategy {
    String format(LogMessage logMessage);

    LogMessage toLogMessage(String input);
}
