package exceptions;

public class ItemNotPurchasedException extends ItemException {

    private String msg;

    public ItemNotPurchasedException(String msg) {
        this.msg = msg;
    }
}
