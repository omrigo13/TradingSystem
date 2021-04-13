package service;

import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;
import user.*;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TradingSystemServiceImplTest {

    private TradingSystemServiceImpl service;

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

    @BeforeEach
    void setUp() {
        service = new TradingSystemServiceImpl(tradingSystem);
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
    void purchaseCart() throws Exception {

        service.purchaseCart(connectionId);
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

        Collection<Item> items = new LinkedList<>();
        items.add(item1);
        items.add(item2);

        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(subscriber.getStoreItems(store)).thenReturn(items);
        when(item1.getName()).thenReturn("item1");
        when(item2.getName()).thenReturn("item2");

        Collection<String> result = service.getItemsByStore(connectionId, storeId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("item1"));
        assertTrue(result.contains("item2"));
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
    void allowManagerToEditPolicies() {
        service.allowManagerToEditPolicies(connectionId,storeId,userName);
    }

    @Test
    void disableManagerFromEditPolicies() {
        service.disableManagerFromEditPolicies(connectionId,storeId,userName);
    }

    @Test
    void allowManagerToGetHistory() {
        service.allowManagerToGetHistory(connectionId,storeId,userName);
    }

    @Test
    void disableManagerFromGetHistory() {
        service.disableManagerFromGetHistory(connectionId,storeId,userName);
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

        service.getEventLog(connectionId);

        verify(subscriber).getEventLog(any());
    }

    @Test
    void getErrorLog() {
        service.getErrorLog(connectionId);
    }
}