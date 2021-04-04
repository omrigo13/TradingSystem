package exceptions;

import exceptions.ItemException;

public class WrongPriceException extends ItemException {

    private String msg;

    public WrongPriceException(String msg) {
        this.msg = msg;
    }
}
