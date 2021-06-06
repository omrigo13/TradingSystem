package user;

import store.Item;
import store.ItemId;
import store.Store;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
@Entity
@IdClass(BasketId.class)

public final class Basket {
    @Id
    @ManyToOne
    private Store store;
    @Transient
    private final Map<Item, Integer> items;
    @Id
    @ManyToOne
    private Subscriber subscriber;

    public Basket(User user, Store store, Map<Item, Integer> items) {
        this.store = store;
        this.items = items;
        if(user instanceof Subscriber)
            subscriber = (Subscriber)user;
    }

    public Basket() {
        items = new ConcurrentHashMap<>();
    }

    public Store getStore() {
        return store;
    }

    public void addItem(Item item, int quantity) {
        items.compute(item, (k, v) -> v == null ? quantity : v + quantity); // add to existing quantity
    }

    public int getQuantity(Item item) {
        return items.getOrDefault(item, 0);
    }

    public void setQuantity(Item item, int quantity) {
        if (quantity == 0)
            removeItem(item);
        else
            items.put(item, quantity);
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public Store store() {
        return store;
    }

    public Map<Item, Integer> items() {
        return items;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Basket) obj;
        return Objects.equals(this.store, that.store) &&
                Objects.equals(this.items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, items);
    }

    @Override
    public String toString() {
        return "Basket[" +
                "store=" + store + ", " +
                "items=" + items + ']';
    }

}
