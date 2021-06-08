package policies;

import user.Basket;

import javax.persistence.Entity;

@Entity
public class UserPolicy extends SimplePurchasePolicy {

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) {
        return false;
    }
}
