package policies;

import user.Basket;

import java.util.Collection;

public class xorPolicy extends compoundPurchasePolicy {

    public xorPolicy(Collection<simplePurchasePolicy> purchasePolicies) {
        super(purchasePolicies);
    }

    @Override
    // only two policies can be in xor and not more then that
    public boolean isValidPurchase(Basket purchaseBasket) {
        if(purchasePolicies.size() == 0)
            return true;
        if(purchasePolicies.size() == 1)
            return purchasePolicies.stream().toList().get(0).isValidPurchase(purchaseBasket);
        if(purchasePolicies.size() == 2) {
            boolean first = purchasePolicies.stream().toList().get(0).isValidPurchase(purchaseBasket);
            boolean second = purchasePolicies.stream().toList().get(1).isValidPurchase(purchaseBasket);
            return ((first && !second) || (!first && second));
        }
        return false;
    }
}
