package policies;

import store.Item;
import user.Basket;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.Map;
@Entity
public class DefaultDiscountPolicy extends SimpleDiscountPolicy {

    public DefaultDiscountPolicy(Collection<Item> items) {
        super(-1, 0, items);
    }

    public DefaultDiscountPolicy() {

    }

    @Override
    public double cartTotalValue(Basket purchaseBasket) {
        double value = 0;
        for(Map.Entry<Item, Integer> itemsAndQuantity: purchaseBasket.getItems().entrySet())
        {
            Item item = itemsAndQuantity.getKey();
            int quantity = itemsAndQuantity.getValue();
            value += (item.getPrice() * quantity);
        }
        return value;
    }
}
