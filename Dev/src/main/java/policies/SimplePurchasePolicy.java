package policies;

import exceptions.PolicyException;
import user.Basket;

import java.util.Collection;
import java.util.LinkedList;

public abstract class SimplePurchasePolicy implements PurchasePolicy {

    public abstract boolean isValidPurchase(Basket purchaseBasket) throws PolicyException;

    public Collection<PurchasePolicy> getPurchasePolicies() { return new LinkedList<>(); }

}
