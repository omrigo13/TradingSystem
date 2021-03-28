package main.java.store;

public class ItemNotFound extends Exception {

    private String msg;

    public ItemNotFound(String msg) {
        this.msg = msg;
    }
}
