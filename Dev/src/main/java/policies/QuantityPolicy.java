package policies;

import exceptions.PolicyException;
import exceptions.QuantityPolicyException;
import store.Item;
import user.Basket;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;
@Entity
public class QuantityPolicy extends SimplePurchasePolicy {
    @ManyToMany
    private Collection<Item> items;
    private int minQuantity;
    private int maxQuantity;

    // if i got 0 in minQuantity or maxQuantity i will ignore it
    public QuantityPolicy(int id, Collection<Item> items, int minQuantity, int maxQuantity) throws QuantityPolicyException {
        super(id);
        this.items = items;
        if(minQuantity < 0 || maxQuantity < 0)
            throw new QuantityPolicyException();
        if((minQuantity > maxQuantity) && (maxQuantity != 0))
            throw new QuantityPolicyException();
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
    }

    public QuantityPolicy() {

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
