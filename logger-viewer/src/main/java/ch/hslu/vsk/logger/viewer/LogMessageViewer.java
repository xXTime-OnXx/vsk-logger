package ch.hslu.vsk.logger.viewer;

import ch.hslu.vsk.logger.common.FinalCompetitionStorageFormatStrategy;
import ch.hslu.vsk.logger.common.JsonMapper;
import ch.hslu.vsk.logger.common.LogMessage;
import ch.hslu.vsk.logger.common.StorageFormatStrategy;

public class LogMessageViewer {

    StorageFormatStrategy storageFormatStrategy = new FinalCompetitionStorageFormatStrategy();

    public void showLogMessage(String message) {
        LogMessage logMessage = JsonMapper.fromString(message, LogMessage.class);
        String formattedLogMessage = storageFormatStrategy.format(logMessage);
        System.out.println(formattedLogMessage);
    }

}
