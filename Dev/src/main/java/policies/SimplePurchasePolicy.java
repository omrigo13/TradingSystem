package policies;

import exceptions.PolicyException;
import user.Basket;

public abstract class SimplePurchasePolicy implements PurchasePolicy {

    public abstract boolean isValidPurchase(Basket purchaseBasket) throws PolicyException;
}
