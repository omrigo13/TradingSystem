package policies;

import exceptions.PolicyException;
import exceptions.QuantityDiscountPolicyException;
import store.Item;
import user.Basket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class QuantityDiscountPolicy extends SimpleDiscountPolicy {

    private final CompoundPurchasePolicy policy;

    public QuantityDiscountPolicy(int discount, Collection<Item> items, CompoundPurchasePolicy policy) throws QuantityDiscountPolicyException {
        super(discount, items);
        if(discount < 0)
            throw new QuantityDiscountPolicyException();
        this.items = items;
        if(policy == null)
        {
            Collection<SimplePurchasePolicy> policies = new ArrayList<>();
            policies.add(new DefaultPurchasePolicy());
            this.policy = new AndPolicy(policies);
        }
        else
            this.policy = policy;
    }

    @Override
    public double cartTotalValue(Basket purchaseBasket) {
        double value = 0.0;
        boolean validPolicy;
        try { validPolicy = policy.isValidPurchase(purchaseBasket); }
        catch (PolicyException p) { validPolicy = false; }
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
