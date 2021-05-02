package policies;

import exceptions.AndPolicyException;
import exceptions.PolicyException;
import user.Basket;

import java.util.Collection;

public class AndPolicy extends CompoundPurchasePolicy {

    public AndPolicy(Collection<PurchasePolicy> purchasePolicies) {
        super(purchasePolicies);
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