package store;

public class WrongPriceException extends ItemException {

    private String msg;

    public WrongPriceException(String msg) {
        this.msg = msg;
    }
}
