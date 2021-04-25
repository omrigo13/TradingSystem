package purchasePolicy;

import user.Basket;

public abstract class simplePurchasePolicy implements purchasePolicy {

    @Override
    public abstract boolean isValidPurchase(Basket purchaseBasket);
}
