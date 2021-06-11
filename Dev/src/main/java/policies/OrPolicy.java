package policies;

import exceptions.OrPolicyException;
import exceptions.PolicyException;
import user.Basket;

import javax.persistence.Entity;
import java.util.Collection;
@Entity
public class OrPolicy extends CompoundPurchasePolicy {

    public OrPolicy(int id, Collection<PurchasePolicy> purchasePolicies) {
        super(id, purchasePolicies);
    }

    public OrPolicy() {
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) throws PolicyException {
        for (PurchasePolicy purchase: purchasePolicies) {
            if(purchase.isValidPurchase(purchaseBasket))
                return true;
        }
        throw new OrPolicyException();
    }
}
