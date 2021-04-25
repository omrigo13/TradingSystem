package user;

import exceptions.ItemException;
import exceptions.NotLoggedInException;
import exceptions.WrongAmountException;
import exceptions.policyException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import policies.*;
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

    private ConcurrentHashMap<Item, Integer> items = new ConcurrentHashMap<>();

    @Spy private Store store;
    @Spy private Item item;
    @Spy private Basket basket = new Basket(store, items);
    @Spy private ConcurrentHashMap<Store, Basket> baskets;

    @Mock private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;

    @BeforeEach
    void setUp() throws ItemException {
        user = new User(baskets);
        basket = new Basket(store, items);
        store.setPurchasePolicy(new defaultPurchasePolicy());
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
        store.addItem("cheese", 7.0, "cat1", "sub1", 5);
        item = store.searchItemById(0);
        baskets.put(store, basket);

        items.put(item, 3);
        assertEquals(1, user.getCart().size());
        assertEquals(5, store.getItems().get(item));
        user.purchaseCart(paymentSystem, deliverySystem);
        assertEquals(0, user.getCart().size()); // checks that the cart is empty after the purchase
        assertEquals(2, store.getItems().get(item)); // checks that the inventory quantity updated
    }

    @Test
    void purchaseCartNegativeQuantity() throws ItemException {
        // trying to purchase negative quantity
        store.addItem("cheese", 7.0, "cat1", "sub1", 5);
        baskets.put(store, basket);
        items.put(item, -2);

        assertThrows(WrongAmountException.class, () -> user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test
    void purchaseCartBigQuantityThanAvailable() throws ItemException {
        // trying to purchase more quantity than available
        store.addItem("cheese", 7.0, "cat1", "sub1", 5);
        baskets.put(store, basket);
        items.put(item, 10);

        assertThrows(WrongAmountException.class, () -> user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test
    void purchaseCartCorrectValueCalculation() throws ItemException, policyException {
        store.addItem("cheese", 7.0, "cat1", "sub1", 5);
        baskets.put(store, basket);
        item = store.searchItemById(0);
        items.put(item, 3);

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("21.0")); // checks that the purchase value correct
    }

    @Test
    void purchaseCartPurchaseHistoryUpdated() throws ItemException , policyException{
        store.addItem("cheese", 7.0, "cat1", "sub1", 5);
        baskets.put(store, basket);
        item = store.searchItemById(0);
        items.put(item, 3);

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("cheese")); // checks that the purchase added to store history
    }
}