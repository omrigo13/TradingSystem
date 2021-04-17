package user;

import exceptions.NotLoggedInException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Inventory;
import store.Item;
import store.Store;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private User user;

    @Mock private Map<Store, Basket> baskets;

    @BeforeEach
    void setUp() {
        user = new User(baskets);
    }

    @Test
    void makeCart_WhenEmpty() {
        when(baskets.isEmpty()).thenReturn(true);
        User from = mock(User.class);
        user.makeCart(from);
        verify(baskets).putAll(any());
    }

    @Test
    void makeCart_WhenNotEmpty() {
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
        when(baskets.get(store)).thenReturn(basket);
        assertEquals(basket, user.getBasket(store));
    }

    @Test
    void purchaseCart() throws Exception {
        PaymentSystem paymentSystem = mock(PaymentSystem.class);
        DeliverySystem deliverySystem = mock(DeliverySystem.class);

        user.purchaseCart(paymentSystem, deliverySystem);

        //TODO empty cart should work for subscriber and user and not only for subscriber
        //TODO if fails nothing done (try to purchase more quantity then available)
    }
}