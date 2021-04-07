package exceptions;

public class ItemNotPurchased extends ItemException {

    private String msg;

    public ItemNotPurchased(String msg) {
        this.msg = msg;
    }
}
