package exceptions;

public class AddStoreItemException extends Exception {

    final String storeName;
    private final String itemName;
    private final double price;
    private final String category;
    private final String subCategory;
    private final int quantity;

    public AddStoreItemException(String storeName, String itemName, double price, String category, String subCategory, int quantity, Exception cause) {
        super(cause);
        this.storeName = storeName;
        this.itemName = itemName;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "AddStoreItemException{" +
                "storeName='" + storeName + '\'' +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", subCategory='" + subCategory + '\'' +
                ", quantity=" + quantity +
                "} " + super.toString();
    }
}
