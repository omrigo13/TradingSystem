package exceptions;

public class WrongReviewException extends Exception{

    private String msg;

    public WrongReviewException(String msg) {
        this.msg = msg;
    }
}


