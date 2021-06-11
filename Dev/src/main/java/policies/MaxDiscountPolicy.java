package policies;

import exceptions.PolicyException;
import store.Item;
import user.Basket;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.Map;
@Entity
public class MaxDiscountPolicy extends CompoundDiscountPolicy {

    public MaxDiscountPolicy(int id, Collection<DiscountPolicy> discountPolicies) {
        super(id, discountPolicies);
    }

    public MaxDiscountPolicy() {
    }

    @Override
    public double cartTotalValue(Basket purchaseBasket) throws PolicyException {
        double value = 0;
        if (discountPolicies.size() == 0) {
            for (Map.Entry<Item, Integer> itemsAndQuantity : purchaseBasket.getItems().entrySet()) {
                Item item = itemsAndQuantity.getKey();
                int quantity = itemsAndQuantity.getValue();
                value += (item.getPrice() * quantity);
            }
            return value;
        }
        value = discountPolicies.stream().toList().get(0).cartTotalValue(purchaseBasket);
        for (DiscountPolicy discountPolicy : discountPolicies) {
            if (discountPolicy.cartTotalValue(purchaseBasket) < value) {
                this.items = discountPolicy.getItems();
                value = discountPolicy.cartTotalValue(purchaseBasket);
                discount = discountPolicy.getDiscount();
            }
        }
        return value;
    }
}
