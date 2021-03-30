package user;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//represents a User's basket, which is connected to maximum 1 store
public class Basket {

    private final String store;
    private final User user;
    private final Map<String, ItemRecord> items = new HashMap<>();

    public static class ItemRecord {
        final String item;
        final int amount;
        public ItemRecord(String item, int amount) {
            this.item = item;
            this.amount = amount;
        }

        public String getItem() {
            return item;
        }

        public int getAmount() {
            return amount;
        }

        @Override
        public String toString() {
            return "" + amount + ", " + item;
        }
    }

    public Basket(String store, User user) {
        this.store = store;
        this.user = user;
    }

    public String getStore() {
        return store;
    }

    public User getUser() {
        return user;
    }

    public void addItem(ItemRecord itemRecord)
    {
        // if the item already exists, add up the amounts
        ItemRecord oldItemRecord = items.get(itemRecord.item);
        if (oldItemRecord != null)
            itemRecord = new ItemRecord(itemRecord.item, itemRecord.amount + oldItemRecord.amount);

        items.put(itemRecord.item, itemRecord);
    }

    public ItemRecord getItem(String item) {
        return items.get(item);
    }

    public Collection<ItemRecord> getItems()
    {
        return items.values();
    }

    public void deleteItem(String item)
    {
        items.remove(item);
    }
}
