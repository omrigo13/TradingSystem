package persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.User;

import static org.junit.jupiter.api.Assertions.*;

class CartsTest {


    private Carts persistence;
    private final String userName = "Lidor";
    private final String password = "lidor12345";
    private final String storeID = "eBay";
    private final String item = "X-Box";
    private final int amount = 1;

    @BeforeEach
    void setUp() {
        persistence = new Carts();
    }

    @Test
    void persistAndRetrieve() {
        User user = new UserMock(userName);
        persistence.persist(user);
        User user1 = new UserMock(userName);
        persistence.retrieve(user1);
        assertEquals(user.getCart(), user1.getCart());
    }
}