package policies;

import exceptions.PolicyException;
import user.Basket;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CompoundPurchasePolicy implements PurchasePolicy {

    protected Collection<PurchasePolicy> purchasePolicies;

    public CompoundPurchasePolicy(Collection<PurchasePolicy> purchasePolicies)
    {
        if(purchasePolicies == null)
            this.purchasePolicies = new ArrayList<>();
        else
            this.purchasePolicies = purchasePolicies;
    }

    public abstract boolean isValidPurchase(Basket purchaseBasket) throws PolicyException;

    public void add(SimplePurchasePolicy purchasePolicy) { this.purchasePolicies.add(purchasePolicy); }

    public void remove(SimplePurchasePolicy purchasePolicy) { this.purchasePolicies.remove(purchasePolicy); }

    public Collection<PurchasePolicy> getPurchasePolicies() { return this.purchasePolicies; }
}
