package policies;

import exceptions.PolicyException;
import user.Basket;

public interface PurchasePolicy {

    boolean isValidPurchase(Basket purchaseBasket) throws PolicyException;

}
