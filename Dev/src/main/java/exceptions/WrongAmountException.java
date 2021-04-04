package exceptions;

import exceptions.ItemException;

public class WrongAmountException extends ItemException {

    private String msg;

    public WrongAmountException(String msg) {
        this.msg = msg;
    }
}
