package policies;

import exceptions.PolicyException;
import user.Basket;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
@Entity
public abstract class CompoundPurchasePolicy extends PurchasePolicy {
    @Transient
    protected Collection<PurchasePolicy> purchasePolicies;

    public CompoundPurchasePolicy( Collection<PurchasePolicy> purchasePolicies)
    {
        super(-1);
        if(purchasePolicies == null)
            this.purchasePolicies = new ArrayList<>();
        else
            this.purchasePolicies = purchasePolicies;
    }

    public CompoundPurchasePolicy() {

    }

    public abstract boolean isValidPurchase(Basket purchaseBasket) throws PolicyException;

    public void add(SimplePurchasePolicy purchasePolicy) { this.purchasePolicies.add(purchasePolicy); }

    public void remove(SimplePurchasePolicy purchasePolicy) { this.purchasePolicies.remove(purchasePolicy); }

    public Collection<PurchasePolicy> getPurchasePolicies() { return this.purchasePolicies; }
}
