package permissions;

import store.Store;

public class ChangeItemCommand extends Command {

    private final int productID;
    private final String newSubCategory;
    private final Integer newQuantity;
    private final Double newPrice;

    public ChangeItemCommand(Store store, int productID, String newSubCategory, Integer newQuantity, Double newPrice) {
        super(store);
        this.productID = productID;
        this.newSubCategory = newSubCategory;
        this.newQuantity = newQuantity;
        this.newPrice = newPrice;
    }

    @Override
    public void doCommand() throws Exception {
        store.changeItem(productID, newSubCategory, newQuantity, newPrice);
    }
}
