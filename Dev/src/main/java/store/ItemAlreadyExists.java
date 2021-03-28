package main.java.store;

public class ItemAlreadyExists extends Exception{

    private String msg;

    public ItemAlreadyExists(String msg) {
        this.msg = msg;
    }
}
