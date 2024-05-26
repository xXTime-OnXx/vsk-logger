package ch.hslu.vsk.logger.common;

import java.util.List;

public interface LogMessagePersistor {
    void save(LogMessage logMessage);
    List<LogMessage> get(int count);
}
