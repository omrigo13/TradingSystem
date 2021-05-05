package exceptions;

public class WrongPriceException extends ItemException {
    public WrongPriceException(String msg) {
        super(msg);
    }
}
