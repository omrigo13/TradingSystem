package policies;

import exceptions.PolicyException;
import user.Basket;

import javax.persistence.*;
import java.util.Collection;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class PurchasePolicy {

    @Id
    private int purchase_id;

    public PurchasePolicy(int purchase_id) {
        this.purchase_id = purchase_id;
    }

    public PurchasePolicy() {
    }

    public int getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(int id) {
        this.purchase_id = id;
    }

    public abstract boolean isValidPurchase(Basket purchaseBasket) throws PolicyException;

    public abstract Collection<PurchasePolicy> getPurchasePolicies();

}
