package exceptions;

public class ItemAlreadyExistsException extends ItemException {

    private String msg;

    public ItemAlreadyExistsException(String msg) {
        this.msg = msg;
    }
}
