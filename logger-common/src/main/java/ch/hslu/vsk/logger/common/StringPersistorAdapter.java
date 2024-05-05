package ch.hslu.vsk.logger.common;

import ch.hslu.vsk.stringpersistor.api.PersistedString;
import ch.hslu.vsk.stringpersistor.api.StringPersistor;
import ch.hslu.vsk.stringpersistor.impl.StringPersistorFactory;

import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

public class StringPersistorAdapter implements StringPersistor {
    private final StringPersistor stringPersistor;

    public StringPersistorAdapter(Path path) {
        this.stringPersistor = StringPersistorFactory.create(path);
    }

    @Override
    public void setFile(Path path) {
        stringPersistor.setFile(path);
    }

    @Override
    public void save(Instant timestamp, String payload) {
        stringPersistor.save(timestamp, payload);
    }

    @Override
    public List<PersistedString> get(int count) {
        return stringPersistor.get(count);
    }

}
