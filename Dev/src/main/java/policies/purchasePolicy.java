package policies;

import user.Basket;

public interface purchasePolicy {

    public abstract boolean isValidPurchase(Basket purchaseBasket);

}
