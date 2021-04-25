package policies;

import exceptions.orPolicyException;
import exceptions.policyException;
import user.Basket;

import java.util.Collection;

public class orPolicy extends compoundPurchasePolicy {

    public orPolicy(Collection<simplePurchasePolicy> purchasePolicies) {
        super(purchasePolicies);
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) throws policyException {
        for (simplePurchasePolicy purchase: purchasePolicies) {
            if(purchase.isValidPurchase(purchaseBasket))
                return true;
        }
        throw new orPolicyException();
    }
}
