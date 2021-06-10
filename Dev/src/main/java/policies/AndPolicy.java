package policies;

import exceptions.AndPolicyException;
import exceptions.PolicyException;
import user.Basket;

import javax.persistence.Entity;
import java.util.Collection;
@Entity
public class AndPolicy extends CompoundPurchasePolicy {

    public AndPolicy(int id, Collection<PurchasePolicy> purchasePolicies) {
        super( id, purchasePolicies);
    }

    public AndPolicy() {
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) throws PolicyException {
        for (PurchasePolicy purchase: purchasePolicies) {
            if(!purchase.isValidPurchase(purchaseBasket))
                throw new AndPolicyException();
        }
        return true;
    }
}
