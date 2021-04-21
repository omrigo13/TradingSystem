package user;

import exceptions.ItemException;
import exceptions.NotLoggedInException;
import exceptions.WrongAmountException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Inventory;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private User user;

    private ConcurrentHashMap<Item, Integer> items;

    @Spy private Store store;
    @Spy private Item item;
    @Spy private Basket basket;
    @Spy private ConcurrentHashMap<Store, Basket> baskets;

    UserTest() throws ItemException {
        items = new ConcurrentHashMap<>();
        store = new Store(0, "eBay", "desc");
        item = new Item(0, "cheese", 7.0, "cat1", "sub1", 5);
        basket = new Basket(store, items);
    }

    @BeforeEach
    void setUp() throws ItemException {
        user = new User(baskets);
        store.addItem("cheese", 7.0, "cat1", "sub1", 5);
        item = store.searchItemById(0);
    }

    @Test
    void makeCart_WhenEmpty() {
        User from = mock(User.class);
        user.makeCart(from);
        verify(baskets).putAll(any());
    }

    @Test
    void makeCart_WhenNotEmpty() {
        when(baskets.isEmpty()).thenReturn(false);
        User from = mock(User.class);
        user.makeCart(from);
        verify(baskets, never()).putAll(any());
    }

    @Test
    void getSubscriber() {
        assertThrows(NotLoggedInException.class, () -> user.getSubscriber());
    }

    @Test
    void getNewBasket() {
        Store store = mock(Store.class);
        assertNotNull(user.getBasket(store));
    }

    @Test
    void getExistingBasket() {
        Store store = mock(Store.class);
        Basket basket = mock(Basket.class);
        baskets.put(store, basket);
        assertSame(basket, user.getBasket(store));
    }

    @Test
    void purchaseCart() throws Exception {
        PaymentSystem paymentSystem = mock(PaymentSystem.class);
        DeliverySystem deliverySystem = mock(DeliverySystem.class);

        baskets.put(store, basket);

        // trying to purchase more quantity than available
        items.put(item, 10);
        assertThrows(WrongAmountException.class, () -> user.purchaseCart(paymentSystem, deliverySystem));

        // trying to purchase negative quantity
        basket.setQuantity(item, -2);
        assertThrows(WrongAmountException.class, () -> user.purchaseCart(paymentSystem, deliverySystem));

        basket.setQuantity(item, 3);
        assertEquals(1, user.getCart().size());
        assertEquals(5, store.getItems().get(item));
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("21.0")); // checks that the purchase value correct
        assertTrue(store.getPurchaseHistory().toString().contains("cheese")); // checks that the purchase added to store history
        assertEquals(0, user.getCart().size()); // checks that the cart is empty after the purchase
        assertEquals(2, store.getItems().get(item)); // checks that the inventory quantity updated
    }
}