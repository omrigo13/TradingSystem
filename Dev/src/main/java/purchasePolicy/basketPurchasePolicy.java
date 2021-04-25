package purchasePolicy;

import store.Item;
import user.Basket;

import java.util.Collection;

public class basketPurchasePolicy extends simplePurchasePolicy {

    private Basket basket;
    private Item item;
    private int minQuantity;
    private int maxQuantity;

    // if i got 0 in minQuantity or maxQuantity i will ignore it
    public basketPurchasePolicy(Basket basket, Item item, int minQuantity, int maxQuantity) {
        this.basket = basket;
        this.item = item;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        return false;
    }
}
