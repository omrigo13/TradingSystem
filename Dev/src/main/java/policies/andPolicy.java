package policies;

import exceptions.andPolicyException;
import exceptions.policyException;
import user.Basket;

import java.util.Collection;

public class andPolicy extends compoundPurchasePolicy {

    public andPolicy(Collection<simplePurchasePolicy> purchasePolicies) {
        super(purchasePolicies);
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) throws policyException {
        for (simplePurchasePolicy purchase: purchasePolicies) {
            if(!purchase.isValidPurchase(purchaseBasket))
                throw new andPolicyException();
        }
        return true;
    }
}
