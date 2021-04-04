package exceptions;

import exceptions.ItemException;

public class WrongRatingException extends ItemException {

    private String msg;

    public WrongRatingException(String msg) {
        this.msg = msg;
    }
}
