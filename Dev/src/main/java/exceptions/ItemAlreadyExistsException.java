package exceptions;

public class ItemAlreadyExistsException extends ItemException {
    public ItemAlreadyExistsException(String msg) {
        super(msg);
    }
}
