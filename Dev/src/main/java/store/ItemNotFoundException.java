package store;

public class ItemNotFoundException extends ItemException {

    private String msg;

    public ItemNotFoundException(String msg) {
        this.msg = msg;
    }
}
