package policies;

import exceptions.policyException;
import store.Item;
import user.Basket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class plusDiscountPolicy extends compoundDiscountPolicy {

    public plusDiscountPolicy(Collection<discountPolicy> discountPolicies) {
        super(discountPolicies);
        for (discountPolicy discountPolicy: discountPolicies) {
            items.addAll(discountPolicy.getItems());
        }
    }

    @Override
    public double cartTotalValue(Basket purchaseBasket) throws policyException {
        double value = 0;
        if(discountPolicies.size() == 0) {
            for(Map.Entry<Item, Integer> itemsAndQuantity: purchaseBasket.getItems().entrySet())
            {
                Item item = itemsAndQuantity.getKey();
                int quantity = itemsAndQuantity.getValue();
                value += (item.getPrice() * quantity);
            }
            return value;
        }
        if(discountPolicies.size() == 1)
            return discountPolicies.stream().toList().get(0).cartTotalValue(purchaseBasket);
        for(Map.Entry<Item, Integer> itemsAndQuantity: purchaseBasket.getItems().entrySet()) {
            Item item = itemsAndQuantity.getKey();
            int quantity = itemsAndQuantity.getValue();
            int totalDiscount = 0;
            Collection<Item> items = new ArrayList<>();
            items.add(item);
            for (discountPolicy discountPolicy : discountPolicies) {
                discountPolicy.cartTotalValue(purchaseBasket);
                if(discountPolicy.getItems().contains(item))
                    totalDiscount += discountPolicy.getDiscount();
            }
            value += ((((100 - (double)totalDiscount) / 100) * item.getPrice()) * quantity);
        }
        return value;
    }
}
