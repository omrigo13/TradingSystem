package service;

import authentication.UserAuthentication;
import exceptions.*;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import purchaseAndReview.Purchase;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;
import user.Basket;
import user.ManagerPermission;
import user.Subscriber;
import user.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class TradingSystemServiceImpl implements TradingSystemService {

    TradingSystem tradingSystem;

    final UserAuthentication auth;
    final PaymentSystem paymentSystem;
    final DeliverySystem deliverySystem;
    final Map<String, Subscriber> subscribers;
    final Map<String, User> connections;
    final Map<Integer, Store> stores;

    public TradingSystemServiceImpl(UserAuthentication auth, PaymentSystem paymentSystem, DeliverySystem deliverySystem,
                                    Map<String, Subscriber> subscribers, Map<String, User> connections, Map<Integer, Store> stores) {
        this.auth = auth;
        this.paymentSystem = paymentSystem;
        this.deliverySystem = deliverySystem;
        this.subscribers = subscribers;
        this.connections = connections;
        this.stores = stores;
    }

    @Override
    public void initializeSystem(String userName, String pass) throws LoginException {
        tradingSystem = TradingSystem.createTradingSystem(userName, pass, paymentSystem, deliverySystem, auth,
                subscribers, connections, stores);
    }

    @Override
    public String connect() {
        return tradingSystem.connect();
    }

    @Override
    public void register(String userName, String password) throws SubscriberAlreadyExistsException {
        auth.register(userName, password);
    }

    @Override
    public void login(String connectionId, String userName, String pass) throws LoginException {
        tradingSystem.login(connectionId, userName, pass);
    }

    @Override
    public void logout(String connectionId) throws ConnectionIdDoesNotExistException {
        tradingSystem.logout(connectionId);
    }

    @Override
    public Collection<String> getItems(String keyWord, String productName, String category, String subCategory,
                                       Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) {

        return tradingSystem.getItems(keyWord,productName,category,subCategory,ratingItem,ratingStore,maxPrice,minPrice);
    }

    @Override
    public void addItemToBasket(String connectionId, String storeId, String productId, int quantity) throws ConnectionIdDoesNotExistException, ItemException {
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).addItem(item, quantity);
    }

    @Override
    public Collection<String> showCart(String connectionId) throws ConnectionIdDoesNotExistException {
        Collection<String> itemList = new LinkedList<>();
        Map<Store, Basket> cart = tradingSystem.getUserByConnectionId(connectionId).getCart();
        for (Map.Entry<Store, Basket> storeBasketEntry : cart.entrySet()) {
            Store store = storeBasketEntry.getKey();
            String storeName = store.getName();
            Map<Item, Integer> items = storeBasketEntry.getValue().getItems();

            for (Map.Entry<Item, Integer> itemQuantityEntry : items.entrySet()) {
                Item item = itemQuantityEntry.getKey();
                Integer quantity = itemQuantityEntry.getValue();
                String itemString = "Store: " + storeName + " Item: " + item.getName() + " Quantity: " + quantity;
                itemList.add(itemString);
            }
        }


        return itemList;
    }

    @Override
    public Collection<String> showBasket(String connectionId, String storeId) throws ConnectionIdDoesNotExistException {
        Collection<String> itemList = new LinkedList<>();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Basket basket = tradingSystem.getUserByConnectionId(connectionId).getBasket(store);
        for (Map.Entry<Item, Integer> itemQuantityEntry : basket.getItems().entrySet()) {
            Item item = itemQuantityEntry.getKey();
            Integer quantity = itemQuantityEntry.getValue();
            String itemString = "Store: " + store.getName() + " Item: " + item.getName() + " Quantity: " + quantity;
            itemList.add(itemString);
        }
        return itemList;
    }

    @Override
    public void updateProductAmountInBasket(String connectionId, String storeId, String productId, int quantity) throws ConnectionIdDoesNotExistException, ItemException {
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).setQuantity(item, quantity);
    }
    @Override
    public void purchaseCart(String connectionId) throws ConnectionIdDoesNotExistException, ExternalServicesException, Exception {
        tradingSystem.purchaseCart(connectionId);
    }

    @Override
    public Collection<String> getPurchaseHistory(String connectionId) throws ConnectionIdDoesNotExistException {
        User user = tradingSystem.getUserByConnectionId(connectionId);
        Collection<String> purchases = new LinkedList<>();
        for (Purchase purchase: user.getPurchases()) {
            purchases.add(purchase.getDetails());
        }
        return purchases;
    } //TODO add a permission to subscriber user to see his history

    @Override
    public void writeOpinionOnProduct(String connectionId, String storeID, String productId, String desc) throws ConnectionIdDoesNotExistException, ItemException {
        tradingSystem.writeOpinionOnProduct(connectionId, storeID, productId, desc);
    }

    @Override
    public Collection<String> getStoresInfo(String connectionId) throws ConnectionIdDoesNotExistException, NotLoggedInException, NoPermissionException {
        Collection<String> infoList = new LinkedList<>();
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        for (Store store : subscriber.getAllStores(tradingSystem.getStores()))
            infoList.add(store.toString());
        return infoList;
    }

    @Override
    public Collection<String> getItemsByStore(String connectionId, String storeId) throws ConnectionIdDoesNotExistException, NotLoggedInException, NoPermissionException {
        Collection<String> itemList = new LinkedList<>();
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        for (Item item : subscriber.getStoreItems(store))
            itemList.add(item.getName());
        return itemList;
    }

    @Override
    public String openNewStore(String connectionId, String newStoreName) throws ConnectionIdDoesNotExistException, NotLoggedInException, NewStoreException {
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        return "" + tradingSystem.newStore(subscriber, newStoreName);
    }

    @Override
    public void appointStoreManager(String connectionId, String targetUserName, String storeId)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, AlreadyOwnerException {
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addManagerPermission(target, store);
    }

    @Override
    public String addProductToStore(String connectionId, String storeId, String itemName, String category, String subCategory, int quantity, double price)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, AddStoreItemException, GetStoreItemException {
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        int itemId=subscriber.addStoreItem(store, itemName, category, subCategory, quantity, price);
        Item item;
        try {

            item = store.searchItemById(itemId);
        } catch (Exception e) {
            throw new GetStoreItemException(store.getName(), itemName, category, subCategory, e);
        }
        return "" + itemId;
    }

    @Override
    public void deleteProductFromStore(String connectionId, String storeId, String itemId)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, RemoveStoreItemException {
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeStoreItem(store, Integer.parseInt(itemId));
    }

    @Override
    public void updateProductDetails(String connectionId, String storeId, String itemId, String newSubCategory, Integer newQuantity, Double newPrice)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, UpdateStoreItemException {
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.updateStoreItem(store, Integer.parseInt(itemId), newSubCategory, newQuantity, newPrice);
    }

    @Override
    public void appointStoreOwner(String connectionId, String targetUserName, String storeId)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, AlreadyOwnerException {
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addOwnerPermission(target, store);
    }

    @Override
    public void allowManagerToUpdateProducts(String connectionId, String storeId, String targetUserName)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, TargetIsNotStoreManagerException {
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addInventoryManagementPermission(target, store);
    }

    @Override
    public void disableManagerFromUpdateProducts(String connectionId, String storeId, String targetUserName)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException {
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeInventoryManagementPermission(target, store);
    }

    @Override
    public void allowManagerToEditPolicies(String connectionId, String storeId, String managerUserName) {

    }

    @Override
    public void disableManagerFromEditPolicies(String connectionId, String storeId, String managerUserName) {

    }

    @Override
    public void allowManagerToGetHistory(String connectionId, String storeId, String managerUserName) {

    }

    @Override
    public void disableManagerFromGetHistory(String connectionId, String storeId, String managerUserName) {

    }

    @Override
    public boolean removeManager(String connectionId, String storeId, String targetUserName)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException {
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        if (!target.havePermission(ManagerPermission.getInstance(store)))
            return false;
        subscriber.removeManagerPermission(target, store);
        return true;
    }

    @Override
    public Collection<String> showStaffInfo(String connectionId, String storeId)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException {
        Subscriber subscriber = tradingSystem.getSubscriberByConnectionId(connectionId);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Collection<Subscriber> staff = tradingSystem.getStoreStaff(subscriber, store, new LinkedList<>());
        Collection<String> staffList = new LinkedList<>();
        for (Subscriber staffMember : staff)
            staffList.add(staffMember.storePermissionsToString(store));
        return staffList;
    }

    @Override
    public Collection<String> getSalesHistoryByStore(String connectionId, String storeId) throws ConnectionIdDoesNotExistException {
        //TODO admin permission to see all stores history new function to add use case 6.4
        //TODO store owner get a permission to see store purchase history and he can add a permission to a manager to see the store history also
        Collection<String> purchases = new LinkedList<>();
        for (Purchase purchase: tradingSystem.getStore(Integer.parseInt(storeId)).getPurchases()) {
            purchases.add(purchase.getDetails());
        }
        return purchases;
    }

    @Override
    public Collection<String> getEventLog(String connectionId) {
        return null;
    }

    @Override
    public Collection<String> getErrorLog(String connectionId) {
        return null;
    }
}
