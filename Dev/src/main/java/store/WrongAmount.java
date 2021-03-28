package main.java.store;

public class WrongAmount extends Exception {

    private String msg;

    public WrongAmount(String msg) {
        this.msg = msg;
    }
}
