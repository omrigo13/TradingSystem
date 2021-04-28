package policies;

import exceptions.OrPolicyException;
import exceptions.PolicyException;
import user.Basket;

import java.util.Collection;

public class OrPolicy extends CompoundPurchasePolicy {

    public OrPolicy(Collection<SimplePurchasePolicy> purchasePolicies) {
        super(purchasePolicies);
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) throws PolicyException {
        for (SimplePurchasePolicy purchase: purchasePolicies) {
            if(purchase.isValidPurchase(purchaseBasket))
                return true;
        }
        throw new OrPolicyException();
    }
}
