package purchasePolicy;

import user.Basket;

import java.util.Collection;

public class orPurchasePolicy extends compoundPurchasePolicy {
    public orPurchasePolicy(Collection<simplePurchasePolicy> purchasePolicies) {
        super(purchasePolicies);
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        for (purchasePolicy purchase: purchasePolicies) {
            if(purchase.isValidPurchase(purchaseBasket))
                return true;
        }
        return false;
    }

}
