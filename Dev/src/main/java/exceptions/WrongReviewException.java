package exceptions;

public class WrongReviewException extends InvalidActionException {

    private String msg;

    public WrongReviewException(String msg) {
        this.msg = msg;
    }
}


