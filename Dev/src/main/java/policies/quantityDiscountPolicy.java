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

    private final int discount;
    private final Collection<Item> items;
    private final compoundPurchasePolicy policy;

    public quantityDiscountPolicy(int discount, Collection<Item> items, compoundPurchasePolicy policy) throws quantityDiscountPolicyException {
        this.discount = discount;
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
    public double calculateDiscount(Basket purchaseBasket) throws policyException {
        double value = 0.0;
        boolean validPolicy;
        try { validPolicy = policy.isValidPurchase(purchaseBasket); }
        catch (policyException p) { validPolicy = false; }
        if(!validPolicy)
            return value;
        for(Map.Entry<Item, Integer> itemsAndQuantity: purchaseBasket.getItems().entrySet())
        {
            Item item = itemsAndQuantity.getKey();
            int quantity = itemsAndQuantity.getValue();
            if(items.contains(item))
                value += (((discount / 100) * item.getPrice()) * quantity);
        }
        return value;
    }

    @Override
    //TODO we want to update the item's price only on the basket or also on the store?
    //TODO maybe to make that when a store set a discount policy it updates all the items in the store according to the policy
    public void updateBasket(Basket purchaseBasket) throws policyException, ItemException {
        boolean validPolicy;
        try { validPolicy = policy.isValidPurchase(purchaseBasket); }
        catch (policyException p) { validPolicy = false; }
        if(!validPolicy)
            return;
        for(Item item: purchaseBasket.getItems().keySet())
            if(items.contains(item))
                item.setPrice(((100 - (double)discount) / 100) * item.getPrice());
    }
}
