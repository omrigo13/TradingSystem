package policies;

import exceptions.PolicyException;
import user.Basket;

import java.util.Collection;

public interface PurchasePolicy {

    boolean isValidPurchase(Basket purchaseBasket) throws PolicyException;

    Collection<PurchasePolicy> getPurchasePolicies();

}
