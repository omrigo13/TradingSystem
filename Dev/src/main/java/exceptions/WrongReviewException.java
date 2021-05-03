package exceptions;

public class WrongReviewException extends InvalidActionException {
    public WrongReviewException(String msg) {
        super(msg);
    }
}


