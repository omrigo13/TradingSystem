package policies;

import exceptions.PolicyException;
import exceptions.QuantityDiscountPolicyException;
import store.Item;
import user.Basket;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
@Entity
public class QuantityDiscountPolicy extends SimpleDiscountPolicy {

    @ManyToOne
    private PurchasePolicy policy;

    public QuantityDiscountPolicy(int id, int discount, Collection<Item> items, PurchasePolicy policy) throws QuantityDiscountPolicyException {
        super(id, discount, items);
        if(discount < 0)
            throw new QuantityDiscountPolicyException();
        this.items = items;
        if(policy == null)
        {
            Collection<PurchasePolicy> policies = new LinkedList<>();
            policies.add(new DefaultPurchasePolicy());
            this.policy = null; //todo: Omri
//            this.policy = new AndPolicy(policies);

        }
        else
            this.policy = policy;
    }

    public QuantityDiscountPolicy() {
    }

    @Override
    public double cartTotalValue(Basket purchaseBasket) {
        double value = 0.0;
        boolean validPolicy;
        try { validPolicy = policy.isValidPurchase(purchaseBasket); }
        catch (PolicyException p) { validPolicy = false; }
        catch (NullPointerException e) { validPolicy = true; }
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
