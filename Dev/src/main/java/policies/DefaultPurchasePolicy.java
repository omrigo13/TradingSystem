package policies;

import user.Basket;

import javax.persistence.Entity;

@Entity
public class DefaultPurchasePolicy extends SimplePurchasePolicy {

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) { return true; }
}
