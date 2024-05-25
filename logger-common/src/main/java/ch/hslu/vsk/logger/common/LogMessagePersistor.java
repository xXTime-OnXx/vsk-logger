package ch.hslu.vsk.logger.common;

import ch.hslu.vsk.stringpersistor.api.PersistedString;

import java.util.List;

public interface LogMessagePersistor {
    void save(LogMessage logMessage);
    List<PersistedString> get(int count);
}
