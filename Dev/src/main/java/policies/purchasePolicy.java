package policies;

import exceptions.policyException;
import user.Basket;

public interface purchasePolicy {

    boolean isValidPurchase(Basket purchaseBasket) throws policyException;

}
