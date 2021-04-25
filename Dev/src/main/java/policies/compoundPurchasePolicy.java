package policies;

import exceptions.policyException;
import user.Basket;

import java.util.ArrayList;
import java.util.Collection;

public abstract class compoundPurchasePolicy implements purchasePolicy{

    protected Collection<simplePurchasePolicy> purchasePolicies;

    public compoundPurchasePolicy(Collection<simplePurchasePolicy> purchasePolicies)
    {
        if(purchasePolicies == null)
            this.purchasePolicies = new ArrayList<>();
        else
            this.purchasePolicies = purchasePolicies;
    }

    public abstract boolean isValidPurchase(Basket purchaseBasket) throws policyException;

    public void add(simplePurchasePolicy purchasePolicy) { this.purchasePolicies.add(purchasePolicy); }

    public void remove(simplePurchasePolicy purchasePolicy) { this.purchasePolicies.remove(purchasePolicy); }

    public Collection<simplePurchasePolicy> getPurchasePolicies() { return this.purchasePolicies; }
}
