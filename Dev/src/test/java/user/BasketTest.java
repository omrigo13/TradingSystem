package user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.Basket.ItemRecord;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class BasketTest {

    final Collection<String> userNames = new HashSet<>();
    final User user = new User(userNames);
    final String store = "Store";
    final String item = "Item";
    final int amount = 2;
    final ItemRecord itemRecord = new ItemRecord(item, amount);

    @Test
    void addAndGetItem() {
        Basket basket = new Basket(store, user);
        basket.addItem(itemRecord);
        Collection<ItemRecord> items = basket.getItems();
        assertTrue(items.contains(itemRecord));
    }

    @Test
    void addExistingItem() {
        Basket basket = new Basket(store, user);
        basket.addItem(itemRecord);
        basket.addItem(itemRecord);
        ItemRecord newItemRecord = basket.getItem(item);
        assertEquals(newItemRecord.amount, itemRecord.amount * 2);
    }

    @Test
    void deleteItem() {
        Basket basket = new Basket(store, user);
        basket.addItem(itemRecord);
        basket.deleteItem(itemRecord.item);
        ItemRecord newItemRecord = basket.getItem(item);
        assertNull(newItemRecord);
    }

    @Test
    void deleteItemNotInEmptyBasket() {
        Basket basket = new Basket(store, user);
        basket.deleteItem(itemRecord.item);
        ItemRecord newItemRecord = basket.getItem(item);
        assertNull(newItemRecord);
    }

    @Test
    void deleteItemNotInBasket() {
        Basket basket = new Basket(store, user);
        basket.addItem(new ItemRecord("Another item", 1));
        basket.deleteItem(itemRecord.item);
        ItemRecord newItemRecord = basket.getItem(item);
        assertNull(newItemRecord);
    }
}