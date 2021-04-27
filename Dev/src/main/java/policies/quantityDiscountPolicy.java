package policies;

import exceptions.ItemException;
import exceptions.policyException;
import exceptions.quantityDiscountPolicyException;
import exceptions.quantityPolicyException;
import store.Item;
import user.Basket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class quantityDiscountPolicy extends simpleDiscountPolicy {

    private final compoundPurchasePolicy policy;

    public quantityDiscountPolicy(int discount, Collection<Item> items, compoundPurchasePolicy policy) throws quantityDiscountPolicyException {
        super(discount, items);
        if(discount < 0)
            throw new quantityDiscountPolicyException();
        this.items = items;
        if(policy == null)
        {
            Collection<simplePurchasePolicy> policies = new ArrayList<>();
            policies.add(new defaultPurchasePolicy());
            this.policy = new andPolicy(policies);
        }
        else
            this.policy = policy;
    }

    @Override
    public double cartTotalValue(Basket purchaseBasket) {
        double value = 0.0;
        boolean validPolicy;
        try { validPolicy = policy.isValidPurchase(purchaseBasket); }
        catch (policyException p) { validPolicy = false; }
        for(Map.Entry<Item, Integer> itemsAndQuantity: purchaseBasket.getItems().entrySet())
        {
            Item item = itemsAndQuantity.getKey();
            int quantity = itemsAndQuantity.getValue();
            if(items.contains(item) && validPolicy)
                value += ((((100 - (double)discount) / 100) * item.getPrice()) * quantity);
            else
                value += (item.getPrice() * quantity);
        }
        return value;
    }
}
