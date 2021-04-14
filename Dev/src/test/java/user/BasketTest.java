package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Item;
import store.Store;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BasketTest {

    private Basket basket;

    @Mock Store store;
    @Mock Item item;

    private final ConcurrentHashMap<Item, Integer> items = new ConcurrentHashMap<>();

    private final int quantity = 3;
    private final int differentQuantity = 5;

    @BeforeEach
    void setUp() {
        items.clear();
        items.put(item, quantity);
        basket = new Basket(store, items);
    }

    @Test
    void addItem_notInBasket() {
        items.clear();
        basket.addItem(item, quantity);
        assertEquals(quantity, items.get(item));
    }

    @Test
    void addItem_alreadyInBasket() {
        basket.addItem(item, differentQuantity);
        assertEquals(quantity + differentQuantity, items.get(item));
    }

    @Test
    void getQuantity() {
        assertEquals(quantity, basket.getQuantity(item));
    }

    @Test
    void setQuantity_notInBasket() {
        items.clear();
        basket.setQuantity(item, differentQuantity);
        assertEquals(differentQuantity, items.get(item));
    }

    @Test
    void setQuantity_alreadyInBasket() {
        basket.setQuantity(item, differentQuantity);
        assertEquals(differentQuantity, items.get(item));
    }

    @Test
    void removeItem() {
        basket.removeItem(item);
        assertEquals(0, items.size());
    }
}