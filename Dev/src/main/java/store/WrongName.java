package store;

public class WrongName extends Exception {

    private String msg;

    public WrongName(String msg) {
        this.msg = msg;
    }
}
