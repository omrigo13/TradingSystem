package store;

public class WrongAmountException extends ItemException {

    private String msg;

    public WrongAmountException(String msg) {
        this.msg = msg;
    }
}
