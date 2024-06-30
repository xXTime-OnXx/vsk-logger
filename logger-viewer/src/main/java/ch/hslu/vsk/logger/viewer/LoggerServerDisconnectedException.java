package ch.hslu.vsk.logger.viewer;

public class LoggerServerDisconnectedException extends Exception {
    public LoggerServerDisconnectedException(String errorMessage) {
        super(errorMessage);
    }
}
