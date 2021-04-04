package user;

import store.Item;
import store.Store;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Basket {

    private final Store store;
    private final User user;
    private final Map<Item, Integer> items; // item : quantity

    public Basket(Store store, User user, Map<Item, Integer> items) {
        this.store = store;
        this.user = user;
        this.items = items;
    }

    public Store getStore() {
        return store;
    }

    public User getUser() {
        return user;
    }

    public void addItem(Item item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity); // add to existing quantity
    }

    public int getQuantity(Item item) {
        return items.getOrDefault(item, 0);
    }

    public void setQuantity(Item item, int quantity) {
        items.put(item, quantity);
    }

    public Map<Item, Integer> getItems()
    {
        return items;
    }

    public void removeItem(Item item) {
        items.remove(item);
    }
}
