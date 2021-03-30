package store;

public class WrongCategory extends Exception{
    private String msg;

    public WrongCategory(String msg) {
        this.msg = msg;
    }
}
