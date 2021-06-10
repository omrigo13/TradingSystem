package policies;

import exceptions.PolicyException;
import user.Basket;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.util.Collection;
import java.util.LinkedList;
@Entity
public abstract class SimplePurchasePolicy extends PurchasePolicy {

    public SimplePurchasePolicy(int id) {
        super(id);
    }

    public SimplePurchasePolicy() {
    }

    public abstract boolean isValidPurchase(Basket purchaseBasket) throws PolicyException;

    public Collection<PurchasePolicy> getPurchasePolicies() { return new LinkedList<>(); }
}
