package policies;

import exceptions.PolicyException;
import store.Item;
import user.Basket;

import java.util.Collection;

public interface DiscountPolicy {

    double cartTotalValue(Basket purchaseBasket) throws PolicyException;

    int getDiscount();

    Collection<Item> getItems();
}
