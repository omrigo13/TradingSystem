package user;

import exceptions.NotLoggedInException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    private User user;

    @Spy private ConcurrentHashMap<Store, Basket> baskets;

    @BeforeEach
    void setUp() {
        user = new User(baskets);
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
}