package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Item;
import store.Store;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketTest {

    private Basket basket;
    @Mock User user;
    @Mock Store store;
    @Mock Map<Item, Integer> items;
    @Mock Item item;
    private final int quantity = 3;

    @BeforeEach
    void setUp() {
        basket = new Basket(store, user, items);
    }

    @Test
    void addItem() {
        when(items.getOrDefault(item, 0)).thenReturn(quantity);
        basket.addItem(item, quantity);
        verify(items).put(item, 2 * quantity);
    }

    @Test
    void getQuantity() {
        when(items.getOrDefault(item, 0)).thenReturn(quantity);
        assertEquals(quantity, basket.getQuantity(item));
    }

    @Test
    void setQuantity() {
        basket.setQuantity(item, quantity);
        verify(items).put(item, quantity);
    }

    @Test
    void removeItem() {
        basket.removeItem(item);
        verify(items).remove(item);
    }
}