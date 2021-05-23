package notifications;

import store.Item;
import store.Store;
import user.User;

import java.util.Map;

public class PurchaseNotification extends Notification{

    private User buyer = null;
    private Map<Item, Integer> basket = null;
    private Store store = null;

    public PurchaseNotification(Store store, User buyer, Map<Item, Integer> basket) {
        this.store = store;
        this.buyer = buyer;
        this.basket = basket;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
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

    @Override
    public String toString() {
        return "PurchaseNotification{" +
                "buyer=" + buyer +
                ", basket=" + basket +
                '}';
    }

    @Override
    public String print() {
        return "PurchaseNotification{" +
                "buyer=" + buyer +
                ", basket=" + basket +
                '}';
    }
}
