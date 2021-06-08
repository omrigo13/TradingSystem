package notifications;

import exceptions.NotLoggedInException;
import store.Item;
import store.Store;
import user.Subscriber;
import user.User;

import javax.persistence.*;
import java.util.Map;
@Entity
public class PurchaseNotification extends Notification{
    //todo: replace with username/guest string
    @Transient
    private User buyer = null;
    @ElementCollection
    @MapKeyJoinColumns({
            @MapKeyJoinColumn(name="item_id"),
            @MapKeyJoinColumn(name="store_id")
    })
    private Map<Item, Integer> basket = null;
    @ManyToOne
    private Store store = null;

    public PurchaseNotification(Store store, User buyer, Map<Item, Integer> basket) {
        this.store = store;
        this.buyer = buyer;
        this.basket = basket;
    }

    public PurchaseNotification() {

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
            try {
                return "buyer " + buyer.getSubscriber().getUserName() + " purchase basket: " + basket.keySet();
            } catch (NotLoggedInException e) {
                return "buyer guest purchase basket: " + basket.keySet();
            }
    }
}
