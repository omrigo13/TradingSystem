package policies;

import store.Item;
import user.Basket;

import java.util.Collection;

public class quantityPolicy extends simplePurchasePolicy {

    private final Collection<Item> items;
    private final int minQuantity;
    private final int maxQuantity;

    // if i got 0 in minQuantity or maxQuantity i will ignore it
    public quantityPolicy(Collection<Item> items, int minQuantity, int maxQuantity) {
        this.items = items;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        if(minQuantity < 0 || maxQuantity < 0)
            return false;
        if(minQuantity > maxQuantity)
            return false;
        for(Item item: items)
            if(!purchaseBasket.getItems().containsKey(item))
                return false;
        if(minQuantity == 0 && maxQuantity != 0) {
            for(Item item: items)
                if(purchaseBasket.getItems().get(item) > maxQuantity)
                    return false;
            return true;
        }
        if(minQuantity != 0) {
            for(Item item: items)
                if(purchaseBasket.getItems().get(item) < minQuantity)
                    return false;
            return true;
        }
        for(Item item: items)
            if((purchaseBasket.getItems().get(item) < minQuantity) || (purchaseBasket.getItems().get(item) > maxQuantity))
                return false;
        return true;
    }
}
