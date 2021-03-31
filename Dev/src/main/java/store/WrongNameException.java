package store;

public class WrongNameException extends ItemException {

    private String msg;

    public WrongNameException(String msg) {
        this.msg = msg;
    }
}
