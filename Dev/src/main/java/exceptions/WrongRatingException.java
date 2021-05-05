package exceptions;

public class WrongRatingException extends ItemException {
    public WrongRatingException(String msg) {
        super(msg);
    }
}
