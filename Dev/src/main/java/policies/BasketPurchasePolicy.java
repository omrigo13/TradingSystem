package policies;

import exceptions.BasketPurchasePolicyException;
import exceptions.PolicyException;
import store.Item;
import user.Basket;

import java.util.Map;

public class BasketPurchasePolicy extends SimplePurchasePolicy {

    private double cartValue;

    public BasketPurchasePolicy(double cartValue) throws PolicyException {
        this.cartValue = cartValue;
        if(cartValue < 0.0)
            throw new BasketPurchasePolicyException();
    }
    @Override
    public boolean isValidPurchase(Basket purchaseBasket) throws PolicyException {
        double value = 0.0;
        for (Map.Entry<Item, Integer> itemsQuantity: purchaseBasket.getItems().entrySet()) {
            Item item = itemsQuantity.getKey();
            int quantity = itemsQuantity.getValue();
            value += (item.getPrice() * quantity);
        }
        if(value <= cartValue)
            return false;
        return true;
    }
}
