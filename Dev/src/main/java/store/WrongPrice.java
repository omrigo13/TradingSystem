package main.java.store;

public class WrongPrice extends Exception {

    private String msg;

    public WrongPrice(String msg) {
        this.msg = msg;
    }
}
