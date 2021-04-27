package policies;

import exceptions.ItemException;
import exceptions.policyException;
import store.Item;
import user.Basket;

import java.util.Collection;

public interface discountPolicy {

    double cartTotalValue(Basket purchaseBasket) throws policyException;

    int getDiscount();

    Collection<Item> getItems();
}
