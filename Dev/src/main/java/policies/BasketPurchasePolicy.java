package policies;

import exceptions.BasketPurchasePolicyException;
import exceptions.PolicyException;
import store.Item;
import user.Basket;

import javax.persistence.Entity;
import java.util.Map;
@Entity
public class BasketPurchasePolicy extends SimplePurchasePolicy {

    private double cartValue;

    public BasketPurchasePolicy(double cartValue) throws PolicyException {
        this.cartValue = cartValue;
        if(cartValue < 0.0)
            throw new BasketPurchasePolicyException();
    }

    public BasketPurchasePolicy() {

    }

    public double getCartValue() {
        return cartValue;
    }

    public void setCartValue(double cartValue) {
        this.cartValue = cartValue;
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) throws PolicyException {
        double value = 0.0;
        for (Map.Entry<Item, Integer> itemsQuantity: purchaseBasket.getItems().entrySet()) {
            Item item = itemsQuantity.getKey();
            int quantity = itemsQuantity.getValue();
            value += (item.getPrice() * quantity);
        }
        return !(value <= cartValue);
    }
}
