package service;

import exceptions.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;
import tradingSystem.TradingSystemImpl;
import user.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.*;

public class TradingSystemImplTest {

    private TradingSystemImpl service;

    @Mock private TradingSystem tradingSystem;
    @Mock private Map<Store, Basket> cart;
    @Mock private Collection<Store> stores1;
    @Mock private Collection<Subscriber> subscribersCol;
    @Mock private Subscriber subscriber;
    @Mock private Subscriber subscriber1;
    @Mock private Store store;
    @Mock private Store store1;
    @Mock private Item item1;
    @Mock private Item item2;
    @Mock private User user;
    @Mock private Basket basket;

    private final String userName = "Barak";
    private final String password = "1234";
    private final String connectionId = "4523532453245";
    private final String storeId = "43534532";
    private final String itemId = "42341";
    private final int quantity = 3;
    private final String category = "category";
    private final String subCategory = "subCategory";
    private final Double price = 450.6;
    private final String card_number = "1234";
    private final String holder = "a";
    private final String ccv = "001";
    private final String id = "000000018";
    private final String name = "name";
    private final String address = "address";
    private final String city = "city";
    private final String country = "country";
    private final int month = 1;
    private final int year = 2022;
    private final int zip = 12345;

    @BeforeMethod
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new TradingSystemImpl(tradingSystem);
    }

    @Test
    void connect() {

        service.connect();
        verify(tradingSystem).connect();
    }

    @Test
    void register() throws InvalidActionException {

        service.register(userName, password);
        verify(tradingSystem).register(userName, password);
    }

    @Test
    void login() throws InvalidActionException {

        service.login(connectionId, userName, password);
        verify(tradingSystem).login(connectionId, userName, password);
    }

    @Test
    void logout() throws InvalidActionException {

        service.logout(connectionId);
        verify(tradingSystem).logout(connectionId);
    }

    @Test
    void getItems() {

        String keyWord = "key";
        String productName = "product";
        Double ratingItem = 4.5;
        Double ratingStore = 4.0;
        Double maxPrice = 150.0;
        Double minPrice = 10.0;
        service.getItems(keyWord, productName, category, subCategory, ratingItem, ratingStore, maxPrice, minPrice);
        verify(tradingSystem).getItems(keyWord, productName, category, subCategory, ratingItem, ratingStore, maxPrice, minPrice);
    }

    @Test
    void addItemToBasket() throws InvalidActionException {

        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(store.searchItemById(Integer.parseInt(itemId))).thenReturn(item1);
        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getBasket(store)).thenReturn(basket);
        service.addItemToBasket(connectionId, storeId, itemId, quantity);
        verify(basket).addItem(item1, quantity);
    }

    @Test
    void showCart() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getCart()).thenReturn(cart);
        service.showCart(connectionId);
        verify(user).getCart();
    }

    @Test
    void showBasket() throws InvalidActionException {

        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getBasket(store)).thenReturn(basket);
        service.showBasket(connectionId,storeId);
        verify(user).getBasket(store);
    }

    @Test
    void updateProductAmountInBasket() throws InvalidActionException {

        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(store.searchItemById(Integer.parseInt(itemId))).thenReturn(item1);
        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getBasket(store)).thenReturn(basket);
        service.updateProductAmountInBasket(connectionId, storeId, itemId, quantity);
        verify(basket).setQuantity(item1, quantity);
    }

    @Test
    void purchaseCart() throws InvalidActionException {

        service.purchaseCart(connectionId, card_number, month, year, holder, ccv, id, name, address, city, country, zip);
    }

    @Test
    void getPurchaseHistory() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);
        service.getPurchaseHistory(connectionId);
    }

    @Test
    void writeOpinionOnProduct() throws InvalidActionException {

        String review = "good product";
        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(user.getSubscriber()).thenReturn(subscriber);
        service.writeOpinionOnProduct(connectionId, storeId, itemId, review);
        verify(subscriber).writeOpinionOnProduct(store, Integer.parseInt(itemId), review);
    }

    @Test
    void getStoresInfo() throws InvalidActionException {

        Collection<Store> stores = new LinkedList<>();
        stores.add(store);
        stores.add(store1);

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        when(tradingSystem.getStores()).thenReturn(stores1);
        when(subscriber.getAllStores(stores1)).thenReturn(stores);
        when(store.toString()).thenReturn("st");
        when(store1.toString()).thenReturn("st1");

        Collection<String> result = service.getStoresInfo(connectionId);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("st"));
        assertTrue(result.contains("st1"));
    }

    @Test
    void getItemsByStore() throws InvalidActionException {

        Map<Item, Integer> items = new HashMap<>();
        items.put(item1, 1);
        items.put(item2, 1);

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(subscriber.getStoreItems(store)).thenReturn(items);
        when(item1.getName()).thenReturn("item1");
        when(item2.getName()).thenReturn("item2");

        Collection<String> result = service.getItemsByStore(connectionId, storeId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("store: " + storeId + ", item1, quantity: 1"));
        assertTrue(result.contains("store: " + storeId + ", item2, quantity: 1"));
    }

    @Test
    void openNewStore() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        String newStoreName = "Zara";
        service.openNewStore(connectionId, newStoreName);
        verify(tradingSystem).newStore(subscriber, newStoreName);
    }

    @Test
    void appointStoreManager() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.appointStoreManager(connectionId, userName, storeId);
        verify(subscriber).addManagerPermission(subscriber1, store);
    }

    @Test
    void addProductToStore() throws InvalidActionException {

        String itemName = "cucumber";
        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(user.getSubscriber()).thenReturn(subscriber);
        service.addProductToStore(connectionId, storeId, itemName, category, subCategory, quantity, price);
        verify(subscriber).addStoreItem(store, itemName, category, subCategory, quantity, price);
    }

    @Test
    void deleteProductFromStore() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.deleteProductFromStore(connectionId,storeId,"543");
        verify(subscriber).removeStoreItem(store, Integer.parseInt("543"));
    }

    @Test
    void updateProductDetails() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.updateProductDetails(connectionId,storeId,"543",subCategory,quantity,price);
        verify(subscriber).updateStoreItem(store,Integer.parseInt("543"),subCategory,quantity,price);
    }

    @Test
    void appointStoreOwner() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.appointStoreOwner(connectionId, userName, storeId);
        verify(subscriber).addOwnerPermission(subscriber1, store);
    }

    @Test
    void allowManagerToUpdateProducts() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.allowManagerToUpdateProducts(connectionId, storeId, userName);
        verify(subscriber).addInventoryManagementPermission(subscriber1, store);
    }

    @Test
    void disableManagerFromUpdateProducts() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.disableManagerFromUpdateProducts(connectionId,storeId,userName);
        verify(subscriber).removeInventoryManagementPermission(subscriber1, store);
    }

    @Test
    void allowManagerToEditPolicies() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.allowManagerToEditPolicies(connectionId, storeId, userName);
        verify(subscriber).addEditPolicyPermission(subscriber1, store);
    }

    @Test
    void disableManagerFromEditPolicies() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.disableManagerFromEditPolicies(connectionId,storeId,userName);
        verify(subscriber).removeEditPolicyPermission(subscriber1, store);
    }

    @Test
    void allowManagerToGetHistory() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.allowManagerToGetHistory(connectionId, storeId, userName);
        verify(subscriber).addGetHistoryPermission(subscriber1, store);
    }

    @Test
    void disableManagerFromGetHistory() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.disableManagerFromGetHistory(connectionId,storeId,userName);
        verify(subscriber).removeGetHistoryPermission(subscriber1, store);
    }

    @Test
    void removeManager() throws InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);

        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(subscriber1.havePermission(ManagerPermission.getInstance(store))).thenReturn(true);
        service.removeManager(connectionId,storeId,userName);
        verify(subscriber).removeManagerPermission(subscriber1,store);
    }

    @Test
    void showStaffInfo() throws InvalidActionException {

        Collection<Subscriber> subscribers = new LinkedList<>();
        subscribers.add(subscriber);
        subscribers.add(subscriber1);
        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);
        when(subscriber.getUserName()).thenReturn("Barak");
        when(subscriber1.getUserName()).thenReturn("Lidor");

        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(tradingSystem.getStoreStaff(eq(subscriber), eq(store), any())).thenReturn(subscribers);
        Collection<String> result = service.showStaffInfo(connectionId,storeId);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Barak : " + subscriber.storePermissionsToString(store)));
        assertTrue(result.contains("Lidor : " + subscriber1.storePermissionsToString(store)));
    }

    @Test
    void getEventLog() throws IOException, InvalidActionException {

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);
        Collection<String> log = new LinkedList<>();
        service.getEventLog(connectionId, log);

        verify(subscriber).getEventLog(any());
    }

    @Test
    void getErrorLog() {
        service.getErrorLog(connectionId);
    }
}