package policies;

import user.Basket;

public abstract class simplePurchasePolicy implements purchasePolicy{

    public abstract boolean isValidPurchase(Basket purchaseBasket);
}
