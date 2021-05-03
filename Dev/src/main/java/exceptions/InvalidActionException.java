package exceptions;

public class InvalidActionException extends Exception {

    public InvalidActionException() {
    }

    public InvalidActionException(String message) {
        super(message);
    }
}
