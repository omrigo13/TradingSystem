package user;

import store.Item;
import store.Store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public record Basket(Store store, ConcurrentHashMap<Item, Integer> items) {

    public Store getStore() {
        return store;
    }

    public void addItem(Item item, int quantity) {
        items.compute(item, (k, v) -> v  == null ? quantity : v + quantity); // add to existing quantity
    }

    public int getQuantity(Item item) {
        return items.getOrDefault(item, 0);
    }

    public void setQuantity(Item item, int quantity) {
        if(quantity == 0)
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
}
