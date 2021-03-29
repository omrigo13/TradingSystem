package user;

import org.junit.jupiter.api.Test;
import tradingSystem.RegistrationException;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static Collection<String> userNames = new HashSet<>();
    private static String userName = "Barak";
    private static String password = "1456";

    User createAndRegister(String userName, String password) throws RegistrationException {
        User user = new User(new HashSet<>());
        user.register(userName,password);
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
        User user = new User(new HashSet<>());
        assertThrows(LoginNonExistingSubscriberException.class, () -> user.login(userName,password));
    }

    @Test
    void logoutGuest() {
        User user = new User(new HashSet<>());
        assertThrows(LogoutGuestException.class, user::logout);
    }

    @Test
    void getBasket() {
        User user = new User(new HashSet<>());
        String store = "eBay";
        Basket basket = user.getBasket(store);
        assertEquals(user, basket.getUser());
        assertEquals(store, basket.getStore());
    }

    @Test
    void getCart() {
        User user = new User(new HashSet<>());
        Collection<Basket> baskets = user.getCart(); // only tests that no exception is thrown
    }
}