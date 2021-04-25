package purchasePolicy;

import user.Basket;

public class userPurchasePolicy extends simplePurchasePolicy {
    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        return false;
    }
}
