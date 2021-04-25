package policies;

import exceptions.policyException;
import exceptions.quantityPolicyException;
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
    public boolean isValidPurchase(Basket purchaseBasket) throws policyException {
        if(minQuantity < 0 || maxQuantity < 0)
            throw new quantityPolicyException();
        if((minQuantity > maxQuantity) && (maxQuantity != 0))
            throw new quantityPolicyException();
        for(Item item: items)
            if(!purchaseBasket.getItems().containsKey(item))
                throw new quantityPolicyException();
        if(minQuantity == 0 && maxQuantity != 0) {
            for(Item item: items)
                if(purchaseBasket.getItems().get(item) > maxQuantity)
                    throw new quantityPolicyException();
            return true;
        }
        if(minQuantity != 0) {
            for(Item item: items)
                if(purchaseBasket.getItems().get(item) < minQuantity)
                    throw new quantityPolicyException();
            return true;
        }
        for(Item item: items)
            if((purchaseBasket.getItems().get(item) < minQuantity) || (purchaseBasket.getItems().get(item) > maxQuantity))
                throw new quantityPolicyException();
        return true;
    }
}
