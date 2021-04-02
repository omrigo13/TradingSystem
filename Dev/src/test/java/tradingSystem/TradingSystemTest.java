package tradingSystem;

import authentication.*;
import exceptions.*;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Store;
import user.Subscriber;
import user.User;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TradingSystemTest {

    @Mock private UserAuthentication auth;
    @Mock private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;
    @Mock private Map<String, Subscriber> subscribers;
    @Mock private Map<String, User> connections;
    @Mock private Map<String, Store> stores;
    @Mock private User user;

    private final String adminName = "Roni";
    private final String adminPassword = "jsbs03";
    private final String connectionId = "9034580392580932458093248590324850932485";
    private final String userName = "Tal";
    private final String password = "tal123";
    private final String wrongPassword = "76523";
    private final String storeID = "eBay";
    private final String item1 = "X-Box";
    private final int amount1 = 1;
    private final int amount2 = 2;
    private final String item2 = "Play-Station";

    @Test
    void testConstructorException() throws WrongPasswordException, SubscriberDoesNotExistException {
        UserAuthentication auth = mock(UserAuthentication.class);
        PaymentSystem paymentSystem = mock(PaymentSystem.class);
        DeliverySystem deliverySystem = mock(DeliverySystem.class);
        Exception exception;

        doThrow(SubscriberDoesNotExistException.class).when(auth).authenticate(adminName, adminPassword);
        exception = assertThrows(LoginException.class, () -> new TradingSystem(adminName, adminPassword,
                paymentSystem, deliverySystem, auth, new HashMap<>(), new HashMap<>(), new HashMap<>()));
        assertEquals(SubscriberDoesNotExistException.class, exception.getCause().getClass());

        doThrow(WrongPasswordException.class).when(auth).authenticate(adminName, adminPassword);
        exception = assertThrows(LoginException.class, () -> new TradingSystem(adminName, adminPassword,
                paymentSystem, deliverySystem, auth, new HashMap<>(), new HashMap<>(), new HashMap<>()));
        assertEquals(WrongPasswordException.class, exception.getCause().getClass());
    }

    TradingSystem setupTradingSystem() throws LoginException {
        return new TradingSystem(adminName, adminPassword, paymentSystem, deliverySystem, auth, subscribers, connections, stores);
    }

    @Test
    void getUserByConnectionId() throws LoginException, ConnectionIdDoesNotExistException {
        TradingSystem ts = setupTradingSystem();
        assertThrows(ConnectionIdDoesNotExistException.class, () -> ts.getUserByConnectionId(connectionId));
        when(connections.get(connectionId)).thenReturn(user);
        assertEquals(user, ts.getUserByConnectionId(connectionId));
    }

    @Test
    void getUserByName() {
    }

    @Test
    void getStore() {
    }

    @Test
    void connect() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

//    @Test
//    void addAndGetItems() throws UserDoesNotExistException {
//        String guestID = trade.connectGuest();
//        trade.addItemToBasket(guestID, storeID, item1,amount1);
//        trade.addItemToBasket(guestID, storeID, item2,amount2);
//        Basket basket = trade.getUserBasket(guestID, storeID);
//        ItemRecord itemRecord1 = basket.getItem(item1);
//        ItemRecord itemRecord2 = basket.getItem(item2);
//        assertEquals(amount1, itemRecord1.getAmount());
//        assertEquals(amount2, itemRecord2.getAmount());
//    }
//
//    @Test
//    void getItemsNonExistingUser() {
//        String userID = "4hfbhdf583f";
//        assertThrows(UserDoesNotExistException.class, () ->trade.getUserBasket(userID,storeID));
//    }
//
//    @Test
//    void addItemToNonExistingUser() {
//        // adding item to an NonExistingUser user
//        String userID = "2avb65gt";
//        assertThrows(UserDoesNotExistException.class, () ->trade.addItemToBasket(userID, storeID, item1,amount1));
//    }
//
//    @Test
//    void getBasket() throws UserDoesNotExistException {
//        String userID = trade.connectGuest();
//        trade.addItemToBasket(userID, storeID, item1,amount1);
//        Collection<String> items = trade.getBasket(userID, storeID);
//        assertEquals(amount1, items.size());
//        assertTrue(items.contains("1, X-Box"));
//    }
//
//    @Test
//    void getStores() throws UserDoesNotExistException {
//        String userID = trade.connectGuest();
//        trade.addItemToBasket(userID, storeID, item1,amount1);
//        String storeID1 = "Amazon";
//        trade.addItemToBasket(userID, storeID1, item2,amount2);
//        Collection<String> stores = trade.getStores(userID);
//        assertEquals(2, stores.size());
//        assertTrue(stores.contains(storeID));
//        assertTrue(stores.contains(storeID1));
//    }
//
//    @Test
//    void login() throws LoginException, UserAlreadyExistsException {
//        String userID = trade.connectGuest();
//        trade.register(userName, password);
//        trade.login(userID, userName, password);
//    }
//
//    @Test
//    void loginNonExistingUser() {
//        String userID = trade.connectGuest();
//        assertThrows(UserDoesNotExistException.class, () ->trade.login(userID, userName, password));
//    }
//
//    @Test
//    void loginWrongPassword() throws UserAlreadyExistsException {
//        String userID = trade.connectGuest();
//        trade.register(userName, password);
//        assertThrows(WrongPasswordException.class, () ->trade.login(userID, userName, wrongPassword));
//    }
//
//    @Test
//    void logout() throws UserAlreadyExistsException, LoginException, LogoutGuestException {
//        String userID = trade.connectGuest();
//        assertThrows(LogoutGuestException.class, () -> trade.logout(userID));
//        trade.register(userName, password);
//        trade.login(userID, userName, password);
//        trade.logout(userID);
//    }

//    @Test
//    void connectGuest() {
//        TradingSystem tradingSystem = new TradingSystem();
//        String connectID = tradingSystem.connectGuest();
//    }
}