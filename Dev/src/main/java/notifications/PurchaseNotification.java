package notifications;

import store.Item;
import user.User;

import java.util.Map;

public class PurchaseNotification extends Notification{

    private User buyer = null;
    private Map<Item, Integer> basket = null;

    public PurchaseNotification(User buyer, Map<Item, Integer> basket) {
        this.buyer = buyer;
        this.basket = basket;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Map<Item, Integer> getBasket() {
        return basket;
    }

    public void setBasket(Map<Item, Integer> basket) {
        this.basket = basket;
    }

    @Override
    public void notifyNotification() {

    }
}
