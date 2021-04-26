package policies;

import exceptions.basketPurchasePolicyException;
import exceptions.policyException;
import store.Item;
import user.Basket;

import java.util.Map;

public class basketPurchasePolicy extends simplePurchasePolicy {

    private double cartValue;

    public basketPurchasePolicy(double cartValue) throws policyException{
        this.cartValue = cartValue;
        if(cartValue < 0.0)
            throw new basketPurchasePolicyException();
    }
    @Override
    public boolean isValidPurchase(Basket purchaseBasket) throws policyException {
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
