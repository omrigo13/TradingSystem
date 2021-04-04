package exceptions;

import exceptions.ItemException;

public class WrongCategoryException extends ItemException {
    private String msg;

    public WrongCategoryException(String msg) {
        this.msg = msg;
    }
}
