package user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Store;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Mock Map<Store, Basket> baskets;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(baskets);
    }

    @Test
    void getCart() {
        assertEquals(baskets, user.getCart());
    }

    @Test
    void makeCartWhenEmpty() {
        when(baskets.isEmpty()).thenReturn(true);
        User from = mock(User.class);
        user.makeCart(from);
        verify(baskets).putAll(from.getCart());
    }

    @Test
    void makeCartWhenNotEmpty() {
        when(baskets.isEmpty()).thenReturn(false);
        User from = mock(User.class);
        user.makeCart(from);
        verify(baskets, never()).putAll(from.getCart());
    }

    @Test
    void getSubscriber() {
        assertNull(user.getSubscriber());
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
}