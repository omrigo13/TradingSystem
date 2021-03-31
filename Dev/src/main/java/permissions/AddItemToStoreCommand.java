package permissions;

import store.Store;

public class AddItemToStoreCommand extends Command {

    public AddItemToStoreCommand(Store store, String productName, String category, String subCategory, int quantity, double price) {
        super(store);
        this.productName = productName;
        this.category = category;
        this.subCategory = subCategory;
        this.quantity = quantity;
        this.price = price;
    }

    private String productName;
    private String category;
    private String subCategory;
    private int quantity;
    private double price;

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public void doCommand() throws Exception {
        getStore().addItem(productName, price, category, subCategory, quantity);
    }
}
