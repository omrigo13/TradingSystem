package exceptions;

import exceptions.ItemException;

public class ItemNotFoundException extends ItemException {

    private String msg;

    public ItemNotFoundException(String msg) {
        this.msg = msg;
    }
}
