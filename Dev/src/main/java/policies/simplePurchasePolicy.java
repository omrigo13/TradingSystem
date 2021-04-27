package policies;

import exceptions.policyException;
import user.Basket;

public abstract class simplePurchasePolicy implements purchasePolicy{

    public abstract boolean isValidPurchase(Basket purchaseBasket) throws policyException;
}
