package policies;

import user.Basket;

public class defaultPurchasePolicy extends simplePurchasePolicy {

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        return true;
    }
}
