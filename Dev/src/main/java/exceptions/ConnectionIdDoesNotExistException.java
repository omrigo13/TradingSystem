package exceptions;

public class ConnectionIdDoesNotExistException extends Exception {
    final String connectionId;

    public ConnectionIdDoesNotExistException(String connectionId) {
        this.connectionId = connectionId;
    }
}
