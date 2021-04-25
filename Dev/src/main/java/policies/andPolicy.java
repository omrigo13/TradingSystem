package policies;

import user.Basket;

import java.util.Collection;

public class andPolicy extends compoundPurchasePolicy {

    public andPolicy(Collection<simplePurchasePolicy> purchasePolicies) {
        super(purchasePolicies);
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        for (simplePurchasePolicy purchase: purchasePolicies) {
            if(!purchase.isValidPurchase(purchaseBasket))
                return false;
        }
        return true;
    }
}
