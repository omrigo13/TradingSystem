package user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Nested
    @DisplayName("userNames list is empty")
    static class afterRegister{

        // TODO: nested class should be non-static but @BeforeAll is static
        //  (need to check how to do this properly in junit)

        private static Collection<String> userNames = new HashSet<>();
        private static String userName = "Barak";
        private static String password = "1456";

        @BeforeAll
        static void init() throws RegistrationException {
            User user = new User(userNames);
            user.register(userName,password);
        }

        @Test
        void registerExistingSubscriber() {
            User user = new User(userNames);
            assertThrows(SubscriberAlreadyExistsException.class, () -> user.register(userName,password));
        }

        @Test
        void loginSubscriber() throws LoginException {
            User user = new User(userNames);
            user.login(userName,password);
        }

        @Test
        void loginSubscriberAlreadyLoggedIn() throws LoginException {
            User user = new User(userNames);
            user.login(userName,password);
            assertThrows(LoginSubscriberAlreadyLoggedInException.class, () -> user.login(userName,password));
        }

        @Test
        void logout() throws LoginException, LogoutGuestException {
            User user = new User(userNames);
            user.login(userName,password);
            user.logout();
        }

    }

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void registerNewSubscriber() throws RegistrationException {
        String userName = "Tal";
        String password = "tal123";
        User user = new User(new HashSet());
        user.register(userName,password);
    }

    @org.junit.jupiter.api.Test
    void loginNonExistingSubscriber() {
        String userName = "Barak";
        String password = "gth10";
        User user = new User(new HashSet<>());
        assertThrows(LoginNonExistingSubscriberException.class, () -> user.login(userName,password));
    }

    @Test
    void logoutGuest() {
        User user = new User(new HashSet<>());
        assertThrows(LogoutGuestException.class, () -> user.logout());
    }

    @org.junit.jupiter.api.Test
    void getBasket() {
    }

    @org.junit.jupiter.api.Test
    void getCart() {
    }
}