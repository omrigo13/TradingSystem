package policies;

import exceptions.ItemException;
import exceptions.policyException;
import store.Item;
import user.Basket;

import java.util.Collection;

public abstract class simpleDiscountPolicy implements discountPolicy {

    public abstract double calculateDiscount(Basket purchaseBasket) throws policyException;

    public abstract void updateBasket(Basket purchaseBasket) throws policyException, ItemException;
}
