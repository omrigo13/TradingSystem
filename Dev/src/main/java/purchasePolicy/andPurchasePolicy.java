package purchasePolicy;

import user.Basket;

import java.util.Collection;

public class andPurchasePolicy extends compoundPurchasePolicy {

    public andPurchasePolicy(Collection<simplePurchasePolicy> purchasePolicies) {
        super(purchasePolicies);
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        for (purchasePolicy purchase: purchasePolicies) {
            if(!purchase.isValidPurchase(purchaseBasket))
                return false;
        }
        return true;
    }
}
