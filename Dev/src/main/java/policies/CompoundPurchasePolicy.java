package policies;

import exceptions.PolicyException;
import user.Basket;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.Collection;
@Entity
public abstract class CompoundPurchasePolicy extends PurchasePolicy {
    @ManyToMany
    protected Collection<PurchasePolicy> purchasePolicies;

    public CompoundPurchasePolicy(int id, Collection<PurchasePolicy> purchasePolicies)
    {
        super(id);
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
