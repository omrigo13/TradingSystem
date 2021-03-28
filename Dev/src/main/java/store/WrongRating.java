package main.java.store;

public class WrongRating extends Exception {

    private String msg;

    public WrongRating(String msg) {
        this.msg = msg;
    }
}
