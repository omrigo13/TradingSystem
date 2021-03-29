package tradingSystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import user.Basket;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static user.Basket.*;

class TradingSystemTest {


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void registerExistingSubscriber() throws RegistrationException {
        String userName = "Tal";
        String password = "tal123";
        String secondPassword = "76523";
        TradingSystem tradingSystem = new TradingSystem();
        tradingSystem.register(userName, password);
        assertThrows(SubscriberAlreadyExistsException.class, () -> tradingSystem.register(userName,secondPassword));
    }

    @Test
    void registerNewSubscriber() throws RegistrationException {
        String userName = "Tal";
        String password = "tal123";
        TradingSystem tradingSystem = new TradingSystem();
        tradingSystem.register(userName, password);
    }

    @Test
    void addAndGetItems() throws UserDoesNotExistException {
        TradingSystem tradingSystem = new TradingSystem();
        String guestID = tradingSystem.connectGuest();
        String storeID = "eBay";
        String item1 = "X-Box";
        int amount1 = 1;
        int amount2 = 2;
        tradingSystem.addItemToBasket(guestID, storeID, item1,amount1);
        String item2 = "Play-Station";
        tradingSystem.addItemToBasket(guestID, storeID, item2,amount2);
        Basket basket = tradingSystem.getUserBasket(guestID, storeID);
        ItemRecord itemRecord1 = basket.getItem(item1);
        ItemRecord itemRecord2 = basket.getItem(item2);
        assertEquals(amount1, itemRecord1.getAmount());
        assertEquals(amount2, itemRecord2.getAmount());
    }

    @Test
    void getItemsNonExistingUser() {
        TradingSystem tradingSystem = new TradingSystem();
        // adding item to an nonExisting user
        String userID = "4hfbhdf583f";
        String storeID = "eBay";
        assertThrows(UserDoesNotExistException.class, () ->tradingSystem.getUserBasket(userID,storeID));
    }

    @Test
    void addItemToNonExistingUser() {
        TradingSystem tradingSystem = new TradingSystem();
        // adding item to an NonExistingUser user
        String userID = "2avb65gt";
        String storeID = "eBay";
        String item1 = "X-Box";
        int amount1 = 1;
        assertThrows(UserDoesNotExistException.class, () ->tradingSystem.addItemToBasket(userID, storeID, item1,amount1));
    }

    @Test
    void getBasket() throws UserDoesNotExistException {
        TradingSystem tradingSystem = new TradingSystem();
        String userID = tradingSystem.connectGuest();
        String storeID = "eBay";
        String item1 = "X-Box";
        int amount = 1;
        tradingSystem.addItemToBasket(userID, storeID, item1,amount);
        Collection<String> items = tradingSystem.getBasket(userID, storeID);
        assertEquals(1, items.size());
        assertTrue(items.contains("1, X-Box"));
    }

    @Test
    void getStores() throws UserDoesNotExistException {
        TradingSystem tradingSystem = new TradingSystem();
        String userID = tradingSystem.connectGuest();
        String storeID = "eBay";
        String item = "X-Box";
        int amount = 1;
        tradingSystem.addItemToBasket(userID, storeID, item,amount);
        String storeID1 = "Amazon";
        String item1 = "book";
        int amount1 = 2;
        tradingSystem.addItemToBasket(userID, storeID1, item1,amount1);
        Collection<String> stores = tradingSystem.getStores(userID);
        assertEquals(2, stores.size());
        assertTrue(stores.contains(storeID));
        assertTrue(stores.contains(storeID1));
    }

//    @Test
//    void connectGuest() {
//        TradingSystem tradingSystem = new TradingSystem();
//        String connectID = tradingSystem.connectGuest();
//    }
}