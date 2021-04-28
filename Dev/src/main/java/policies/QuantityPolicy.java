package policies;

import exceptions.PolicyException;
import exceptions.QuantityPolicyException;
import store.Item;
import user.Basket;

import java.util.Collection;

public class QuantityPolicy extends SimplePurchasePolicy {

    private final Collection<Item> items;
    private final int minQuantity;
    private final int maxQuantity;

    // if i got 0 in minQuantity or maxQuantity i will ignore it
    public QuantityPolicy(Collection<Item> items, int minQuantity, int maxQuantity) throws QuantityPolicyException {
        this.items = items;
        if(minQuantity < 0 || maxQuantity < 0)
            throw new QuantityPolicyException();
        if((minQuantity > maxQuantity) && (maxQuantity != 0))
            throw new QuantityPolicyException();
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) throws PolicyException {
        for(Item item: items)
            if(!purchaseBasket.getItems().containsKey(item))
                throw new QuantityPolicyException();
        if(minQuantity == 0 && maxQuantity != 0) {
            for(Item item: items)
                if(purchaseBasket.getItems().get(item) > maxQuantity)
                    return false;
            return true;
        }
        if(minQuantity != 0 && maxQuantity == 0) {
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
