package exceptions;

public class WrongAmountException extends ItemException {
    public WrongAmountException(String msg) {
        super(msg);
    }
}
