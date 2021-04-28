package user;

import exceptions.*;
import externalServices.DeliveryData;
import externalServices.DeliverySystem;
import externalServices.PaymentData;
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

    private ConcurrentHashMap<Item, Integer> items = new ConcurrentHashMap<>();

    @Mock private Store store;
    @Mock private Item item;
    @Mock private Basket basket;
    @Mock private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;
    @Mock private PaymentData paymentData;
    @Mock private DeliveryData deliveryData;

    private final ConcurrentHashMap<Store, Basket> baskets = new ConcurrentHashMap<>();

    @BeforeEach
    void setUp() {
        baskets.clear();
        user = spy(new User(baskets));
//        basket = new Basket(store, items);
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
    void purchaseEmptyCart() throws ItemException, ExternalServicesException {

        user.purchaseCart(paymentSystem, deliverySystem);

        verifyNoInteractions(paymentSystem);
        verifyNoInteractions(deliverySystem);
    }

    @Test
    void calculatePaymentData_oneItem() {

        baskets.put(store, basket);
        Map<Item, Integer> items = new HashMap<>();
        items.put(item, 3);
        when(basket.getItems()).thenReturn(items);
        when(item.getPrice()).thenReturn(10.0);

        PaymentData paymentData = user.calculatePaymentData();

        assertEquals(30.0, paymentData.getPaymentValue());
    }

    @Test
    void calculatePaymentData_twoItems() {

        baskets.put(store, basket);
        Map<Item, Integer> items = new HashMap<>();
        items.put(item, 3);
        Item item2 = mock(Item.class);
        items.put(item2, 5);
        when(basket.getItems()).thenReturn(items);
        when(item.getPrice()).thenReturn(10.0);
        when(item2.getPrice()).thenReturn(5.0);

        PaymentData paymentData = user.calculatePaymentData();

        assertEquals(55.0, paymentData.getPaymentValue());
    }

    @Test
    void purchaseCart() throws ItemException, ExternalServicesException {

        baskets.put(store, basket);
        doReturn(paymentData).when(user).calculatePaymentData();
        doReturn(deliveryData).when(user).createDeliveryData();
//        when(user.calculatePaymentData()).thenReturn(paymentData);
//        when(user.createDeliveryData()).thenReturn(deliveryData);
        when(paymentSystem.pay(paymentData)).thenReturn(true);
        when(deliverySystem.deliver(deliveryData)).thenReturn(true);

        user.purchaseCart(paymentSystem, deliverySystem);

        verify(paymentSystem).pay(paymentData);
        verify(deliverySystem).deliver(deliveryData);
    }

    @Test
    void purchaseCart_paymentFailed() {

        baskets.put(store, basket);
        doReturn(paymentData).when(user).calculatePaymentData();
//        doThrow(PaymentSystemException.class).when(paymentSystem).pay(paymentData);

        assertThrows(PaymentSystemException.class, () -> user.purchaseCart(paymentSystem, deliverySystem));

        verifyNoInteractions(deliverySystem);
    }

    @Test
    void purchaseCart_deliveryFailed() {

        baskets.put(store, basket);
        doReturn(paymentData).when(user).calculatePaymentData();
//        when(user.calculatePaymentData()).thenReturn(paymentData);
        when(paymentSystem.pay(paymentData)).thenReturn(true);

        assertThrows(DeliverySystemException.class, () -> user.purchaseCart(paymentSystem, deliverySystem));
    }



    /*
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
    void purchaseCartCorrectValueCalculation() throws ItemException {
        store.addItem("cheese", 7.0, "cat1", "sub1", 5);
        baskets.put(store, basket);
        item = store.searchItemById(0);
        items.put(item, 3);

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("21.0")); // checks that the purchase value correct
    }

    @Test
    void purchaseCartPurchaseHistoryUpdated() throws ItemException {
        store.addItem("cheese", 7.0, "cat1", "sub1", 5);
        baskets.put(store, basket);
        item = store.searchItemById(0);
        items.put(item, 3);

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("cheese")); // checks that the purchase added to store history
    }

    */
}