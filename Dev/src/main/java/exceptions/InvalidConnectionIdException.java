package exceptions;

public class InvalidConnectionIdException extends InvalidActionException {
    final String connectionId;

    public InvalidConnectionIdException(String connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    public String toString() {
        return "InvalidConnectionIdException{" +
                "connectionId='" + connectionId + '\'' +
                '}';
    }
}
