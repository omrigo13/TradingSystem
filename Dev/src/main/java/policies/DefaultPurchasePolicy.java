package policies;

import user.Basket;

public class DefaultPurchasePolicy extends SimplePurchasePolicy {

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) { return true; }
}
