package store;

public class WrongRatingException extends ItemException {

    private String msg;

    public WrongRatingException(String msg) {
        this.msg = msg;
    }
}
