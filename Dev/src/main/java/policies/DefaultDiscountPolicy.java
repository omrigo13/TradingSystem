package policies;

import persistence.Repo;
import store.Item;
import user.Basket;

import javax.persistence.Entity;
import java.util.Map;
@Entity
public class DefaultDiscountPolicy extends SimpleDiscountPolicy {
    private static DefaultDiscountPolicy p = null;

    public DefaultDiscountPolicy() {
        super(-1, 0, null);
    }

    public static DefaultDiscountPolicy getInstance() {
        if(p==null) {
            p = new DefaultDiscountPolicy();
            Repo.merge(p);
        }
        return p;
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
