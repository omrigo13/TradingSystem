package policies;

import user.Basket;

public class UserPolicy extends SimplePurchasePolicy {

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        return false;
    }
}
