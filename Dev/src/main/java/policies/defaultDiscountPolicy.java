package policies;

import store.Item;
import user.Basket;

import java.util.Map;

public class defaultDiscountPolicy extends simpleDiscountPolicy {
    @Override
    public double calculateDiscount(Basket purchaseBasket) {
        return 0;
    }

    @Override
    public void updateBasket(Basket purchaseBasket) {

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
