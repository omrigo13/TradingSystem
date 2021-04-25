package purchasePolicy;

import store.Item;
import user.Basket;

public class itemPurchasePolicy extends simplePurchasePolicy {

    private Item item;
    private int minQuantity;
    private int maxQuantity;

    // if i got 0 in minQuantity or maxQuantity i will ignore it
    public itemPurchasePolicy(Item item, int minQuantity, int maxQuantity) {
        this.item = item;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        if(minQuantity < 0 || maxQuantity < 0)
            return false;
        if(minQuantity > maxQuantity)
            return false;
        if(!purchaseBasket.getItems().containsKey(item))
            return false;
        if(minQuantity == 0 && maxQuantity != 0)
            return purchaseBasket.getItems().get(item) <= maxQuantity;
        if(minQuantity != 0)
            return purchaseBasket.getItems().get(item) >= minQuantity;
        return (purchaseBasket.getItems().get(item) >= minQuantity) && (purchaseBasket.getItems().get(item) <= maxQuantity);
    }
}
