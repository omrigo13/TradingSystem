package policies;

import exceptions.PolicyException;
import user.Basket;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

@Entity
public abstract class CompoundPurchasePolicy extends PurchasePolicy {

    @ManyToMany
    @CollectionTable(name = "compound_purchase_policy_purchase_policies")
    protected Collection<PurchasePolicy> purchasePolicies;

    public CompoundPurchasePolicy(int id, Collection<PurchasePolicy> purchasePolicies)
    {
        super(id);
        if(purchasePolicies == null)
            this.purchasePolicies = new LinkedList<>();
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
