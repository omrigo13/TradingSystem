package tradingSystem;

import authentication.*;
import externalServices.DeliverySystemMock;
import externalServices.PaymentSystemMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.Basket;
import user.LogoutGuestException;
import user.User;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static user.Basket.*;

class TradingSystemTest {
    private TradingSystem trade;
    private final String adminName = "Roni";
    private final String adminPassword = "jsbs03";
    private final String userName = "Tal";
    private final String password = "tal123";
    private final String wrongPassword = "76523";
    private final String storeID = "eBay";
    private final String item1 = "X-Box";
    private final int amount1 = 1;
    private final int amount2 = 2;
    private final String item2 = "Play-Station";

    @BeforeEach
    void setUp() throws LoginException, UserAlreadyExistsException {
        UserAuthentication auth = new UserAuthentication();
        auth.register(adminName, adminPassword);
        trade = new TradingSystem(adminName, adminPassword, new PaymentSystemMock(), new DeliverySystemMock(), auth);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void registerExistingSubscriber() throws RegistrationException {
        trade.register(userName, password);
        assertThrows(UserAlreadyExistsException.class, () -> trade.register(userName, wrongPassword));
    }

    @Test
    void registerNewSubscriber() throws RegistrationException {
        trade.register(userName, password);
    }

    @Test
    void getUser() throws UserIdDoesNotExistException {
        assertThrows(UserIdDoesNotExistException.class, () ->trade.getUser("832475892347589324759823759832"));
        String userID = trade.connectGuest();
        trade.getUser(userID);
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