package ch.hslu.vsk.logger.common;

public class FinalCompetitionStorageFormatStrategy implements StorageFormatStrategy {
    @Override
    public String format(LogMessage logMessage) {

        return String.format(
                "%s %s %s",
                logMessage.getLogLevel().toString().toUpperCase(),
                logMessage.getSource(),
                logMessage.getMessage());
    }

    @Override
    public LogMessage toLogMessage(String input) {
        throw new RuntimeException("Missing Implementation of FinalCompetitionStorageFormatStrategy.toLogMessage(String input)!");
    }
}
