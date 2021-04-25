package purchasePolicy;

import user.Basket;

public interface purchasePolicy {

    boolean isValidPurchase(Basket purchaseBasket);
}
