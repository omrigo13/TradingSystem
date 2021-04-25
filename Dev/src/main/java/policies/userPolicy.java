package policies;

import user.Basket;

public class userPolicy extends simplePurchasePolicy {

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        return false;
    }
}
