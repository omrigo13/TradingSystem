package service;

import authentication.UserAuthentication;
import exceptions.*;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;
import user.Basket;
import user.ManagerPermission;
import user.Subscriber;
import user.User;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TradingSystemServiceImplTest {

    @Mock UserAuthentication auth;
    @Mock PaymentSystem paymentSystem;
    @Mock DeliverySystem deliverySystem;
    @Mock Map<String, Subscriber> subscribers;
    @Mock TradingSystem tradingSystem;
    @Mock Map<String, User> connections;
    @Mock Map<Integer, Store> stores;
    @Mock Map<Store, Basket> cart;
    @Mock Collection<Store> stores1;
    @Mock Collection<Subscriber> subscribersCol;
    @Mock Subscriber subscriber;
    @Mock Subscriber subscriber1;
    @Mock Store store;
    @Mock Store store1;
    @Mock Item item1;
    @Mock Item item2;
    @Mock User user;
    @Mock Basket basket;

    private final String userName = "Barak";
    private final String password = "1234";
    private final String connectionId = "4523532453245";
    private final String storeId = "43534532";
    private final String productId = "42341";
    private final int quantity = 3;
    private final String keyWord = "key";
    private final String productName = "product";
    private final String category = "category";
    private final String subCategory = "subCategory";
    private final Double ratingItem = 4.5;
    private final Double ratingStore = 4.0;
    private final Double maxPrice = 150.0;
    private final Double minPrice = 10.0;
    private final String description = "good product";
    private final String newStoreName = "Zara";
    private final String itemName = "tomato";
    private final Double price = 450.6;

    TradingSystemServiceImpl service;

    @BeforeEach
    void setUp() throws SubscriberDoesNotExistException, WrongPasswordException {

        service = new TradingSystemServiceImpl(auth, paymentSystem, deliverySystem, subscribers, connections, stores);

        try (MockedStatic<TradingSystem> tradingSystemMockedStatic = Mockito.mockStatic(TradingSystem.class)) {
            tradingSystemMockedStatic.when(() -> TradingSystem.createTradingSystem(userName, password, paymentSystem,
                    deliverySystem, auth, subscribers, connections, stores)).thenReturn(tradingSystem);
            service.initializeSystem(userName, password);
        }
    }

        @Test
    void connect() {
        service.connect();
        verify(tradingSystem).connect();
    }

    @Test
    void register() throws SubscriberAlreadyExistsException {
        service.register(userName, password);
        verify(auth).register(userName, password);
    }

    @Test
    void login() throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, WrongPasswordException {
        service.login(connectionId, userName, password);
        verify(tradingSystem).login(connectionId, userName, password);
    }

    @Test
    void logout() throws ConnectionIdDoesNotExistException, NotLoggedInException {
        service.logout(connectionId);
        verify(tradingSystem).logout(connectionId, new User(new HashMap<>()));
    }

    @Test
    void getItems() {
        service.getItems(keyWord, productName, category, subCategory, ratingItem, ratingStore, maxPrice, minPrice);
        verify(tradingSystem).getItems(keyWord, productName, category, subCategory, ratingItem, ratingStore, maxPrice, minPrice);
    }

    @Test
    void addItemToBasket() throws ItemException, ConnectionIdDoesNotExistException, InvalidStoreIdException {
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(store.searchItemById(Integer.parseInt(productId))).thenReturn(item1);
        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getBasket(store)).thenReturn(basket);
        service.addItemToBasket(connectionId, storeId, productId, quantity);
        verify(basket).addItem(item1, quantity);
    }

    @Test
    void showCart() throws ConnectionIdDoesNotExistException {
        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getCart()).thenReturn(cart);
        service.showCart(connectionId);
        verify(user).getCart();
    }

    @Test
    void showBasket() throws ConnectionIdDoesNotExistException, InvalidStoreIdException {
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getBasket(store)).thenReturn(basket);
        service.showBasket(connectionId,storeId);
        verify(user).getBasket(store);
    }

    @Test
    void updateProductAmountInBasket() throws ItemException, ConnectionIdDoesNotExistException, InvalidStoreIdException {
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(store.searchItemById(Integer.parseInt(productId))).thenReturn(item1);
        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
        when(user.getBasket(store)).thenReturn(basket);
        service.updateProductAmountInBasket(connectionId, storeId, productId, quantity);
        verify(basket).setQuantity(item1, quantity);
    }

    @Test
    void purchaseCart() throws Exception {
        service.purchaseCart(connectionId);
    }

    @Test
    void getPurchaseHistory() throws NotLoggedInException, ConnectionIdDoesNotExistException {
        service.getPurchaseHistory(connectionId);
    }

    @Test
    void writeOpinionOnProduct() throws NotLoggedInException, ItemException, ConnectionIdDoesNotExistException, WrongReviewException {
        service.writeOpinionOnProduct(connectionId, storeId, productId, description);
    }

    @Test
    void getStoresInfo() throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException {
        Collection<Store> stores = new LinkedList<>();
        stores.add(store);
        stores.add(store1);

        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
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
    void getItemsByStore() throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, InvalidStoreIdException {

        Collection<Item> items = new LinkedList<>();
        items.add(item1);
        items.add(item2);

        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
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
    void openNewStore() throws NotLoggedInException, ConnectionIdDoesNotExistException, ItemException {
        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
        service.openNewStore(connectionId, newStoreName);
        verify(tradingSystem).newStore(subscriber, newStoreName);
    }

    @Test
    void appointStoreManager() throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, AlreadyOwnerException, InvalidStoreIdException {
        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.appointStoreManager(connectionId, userName, storeId);
        verify(subscriber).addManagerPermission(subscriber1, store);
    }

//    @Test TODO
//    void addProductToStore() throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, AddStoreItemException, ItemException, InvalidStoreIdException {
//        when(tradingSystem.getUserByConnectionId(connectionId)).thenReturn(user);
//        when(user.getSubscriber()).thenReturn(subscriber);
//        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
//        when(subscriber.addStoreItem(store, itemName, category, subCategory, quantity, price)).thenReturn(5);
//        service.addProductToStore(connectionId,storeId,itemName,category,subCategory,quantity,price);
//        verify(store).searchItemById(5);
//    }

    @Test
    void deleteProductFromStore() throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, RemoveStoreItemException, InvalidStoreIdException {
        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.deleteProductFromStore(connectionId,storeId,"543");
        verify(subscriber).removeStoreItem(store, Integer.parseInt("543"));
    }

    @Test
    void updateProductDetails() throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, UpdateStoreItemException, InvalidStoreIdException {
        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.updateProductDetails(connectionId,storeId,"543",subCategory,quantity,price);
        verify(subscriber).updateStoreItem(store,Integer.parseInt("543"),subCategory,quantity,price);
    }

    @Test
    void appointStoreOwner() throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, AlreadyOwnerException, InvalidStoreIdException {
        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.appointStoreOwner(connectionId, userName, storeId);
        verify(subscriber).addOwnerPermission(subscriber1, store);
    }

    @Test
    void allowManagerToUpdateProducts() throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, TargetIsNotStoreManagerException, InvalidStoreIdException {
        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        service.allowManagerToUpdateProducts(connectionId, storeId, userName);
        verify(subscriber).addInventoryManagementPermission(subscriber1, store);
    }

    @Test
    void disableManagerFromUpdateProducts() throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, InvalidStoreIdException {
        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
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
    void removeManager() throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, InvalidStoreIdException {
        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getSubscriberByUserName(userName)).thenReturn(subscriber1);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(subscriber1.havePermission(ManagerPermission.getInstance(store))).thenReturn(true);
        service.removeManager(connectionId,storeId,userName);
        verify(subscriber).removeManagerPermission(subscriber1,store);
    }

    @Test
    void showStaffInfo() throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, InvalidStoreIdException {
        Collection<Subscriber> subscribers = new LinkedList<>();
        subscribers.add(subscriber);
        subscribers.add(subscriber1);
        when(tradingSystem.getUserByConnectionId(connectionId).getSubscriber()).thenReturn(subscriber);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(tradingSystem.getStoreStaff(eq(subscriber), eq(store), any())).thenReturn(subscribers);
        Collection<String> result = service.showStaffInfo(connectionId,storeId);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(subscriber.storePermissionsToString(store)));
        assertTrue(result.contains(subscriber1.storePermissionsToString(store)));
    }

//    @Test
//    void getSalesHistoryByStore() {
//        service.getSalesHistoryByStore(connectionId,storeId);
//    }

    @Test
    void getEventLog() throws IOException {
        service.getEventLog(connectionId);
    }

    @Test
    void getErrorLog() {
        service.getErrorLog(connectionId);
    }
}