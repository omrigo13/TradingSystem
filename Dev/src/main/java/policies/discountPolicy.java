package policies;

import exceptions.ItemException;
import exceptions.policyException;
import user.Basket;

public interface discountPolicy {

    double calculateDiscount(Basket purchaseBasket) throws policyException;

    void updateBasket(Basket purchaseBasket) throws policyException, ItemException;

    double cartTotalValue(Basket purchaseBasket) throws policyException;
}
