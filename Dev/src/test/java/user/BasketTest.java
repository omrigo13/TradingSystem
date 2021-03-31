package user;

import authentication.UserAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.Carts;
import persistence.UserMock;
import user.Basket.ItemRecord;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class BasketTest {

    private Basket basket;
    private final User user = new UserMock("Oz");
    private final String store = "Store";
    private final String item = "Item";
    private final int amount = 2;
    private final ItemRecord itemRecord = new ItemRecord(item, amount);

    @Test
    void addAndGetItem() {
        basket.addItem(itemRecord);
        Collection<ItemRecord> items = basket.getItems();
        assertTrue(items.contains(itemRecord));
    }

    @Test
    void addExistingItem() {
        basket.addItem(itemRecord);
        basket.addItem(itemRecord);
        ItemRecord newItemRecord = basket.getItem(item);
        assertEquals(newItemRecord.amount, itemRecord.amount * 2);
    }

    @Test
    void deleteItem() {
        basket.addItem(itemRecord);
        basket.deleteItem(itemRecord.item);
        ItemRecord newItemRecord = basket.getItem(item);
        assertNull(newItemRecord);
    }

    @Test
    void deleteItemNotInEmptyBasket() {
        basket.deleteItem(itemRecord.item);
        ItemRecord newItemRecord = basket.getItem(item);
        assertNull(newItemRecord);
    }

    @Test
    void deleteItemNotInBasket() {
        basket.addItem(new ItemRecord("Another item", 1));
        basket.deleteItem(itemRecord.item);
        ItemRecord newItemRecord = basket.getItem(item);
        assertNull(newItemRecord);
    }

    @BeforeEach
    void setUp() {
        basket = new Basket(store, user);
    }
}