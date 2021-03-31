package user;

import authentication.LoginException;
import authentication.UserAuthentication;
import authentication.UserDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import authentication.RegistrationException;
import persistence.Carts;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final Carts persistence = new Carts();
    private UserAuthentication auth;
    private static String userName = "Barak";
    private static String password = "1456";

    User createAndRegister(String userName, String password) throws RegistrationException {
        User user = new UserImpl(auth, persistence);
        auth.register(userName,password);
        return user;
    }

    @Test
    void loginSubscriber() throws LoginException, RegistrationException {
        User user = createAndRegister(userName, password);
        user.login(userName,password);
    }

    @Test
    void loginSubscriberAlreadyLoggedIn() throws LoginException, RegistrationException {
        User user = createAndRegister(userName, password);
        user.login(userName,password);
        assertThrows(SubscriberAlreadyLoggedInException.class, () -> user.login(userName,password));
    }

    @Test
    void logout() throws LoginException, LogoutGuestException, RegistrationException {
        User user = createAndRegister(userName, password);
        user.login(userName,password);
        user.logout();
    }

    @Test
    void logoutAndLoginAgain() throws LoginException, LogoutGuestException, RegistrationException {
        User user = createAndRegister(userName, password);
        user.login(userName,password);
        user.logout();
        user.login(userName,password);
    }

    @Test
    void loginNonExistingSubscriber() {
        String userName = "Barak";
        String password = "gth10";
        User user = new UserImpl(auth, persistence);
        assertThrows(UserDoesNotExistException.class, () -> user.login(userName,password));
    }

    @Test
    void logoutGuest() {
        User user = new UserImpl(auth, persistence);
        assertThrows(LogoutGuestException.class, user::logout);
    }

    @Test
    void getBasket() {
        User user = new UserImpl(auth, persistence);
        String store = "eBay";
        Basket basket = user.getBasket(store);
        assertEquals(user, basket.getUser());
        assertEquals(store, basket.getStore());
    }

    @Test
    void getCart() {
        User user = new UserImpl(auth, persistence);
        Collection<Basket> baskets = user.getCart(); // only tests that no exception is thrown
    }

    @BeforeEach
    void setUp() {
        auth = new UserAuthentication();
    }
}