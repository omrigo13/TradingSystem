package tradingSystem;

import exceptions.InvalidActionException;
import exceptions.SubscriberDoesNotExistException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import service.TradingSystemService;
import store.Item;
import store.Store;
import user.AdminPermission;
import user.Basket;
import user.ManagerPermission;
import user.Subscriber;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TradingSystemImpl {

    TradingSystem tradingSystem;

    public TradingSystemImpl(TradingSystem tradingSystem) {

        this.tradingSystem = tradingSystem;

        // TODO this code is a workaround so that admin actions can be tested
        try {
            tradingSystem.register("Admin1", "ad123");
            Subscriber admin = tradingSystem.getSubscriberByUserName("Admin1");
            admin.addPermission(AdminPermission.getInstance());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String connect() {
        return tradingSystem.connect();
    }

    public Subscriber getSubscriberByUserName(String userName) throws InvalidActionException {

        return tradingSystem.getSubscriberByUserName(userName);
    }

    public void register(String userName, String password) throws InvalidActionException {
        tradingSystem.register(userName, password);
    }

    public void login(String connectionId, String userName, String pass) throws InvalidActionException {
        tradingSystem.login(connectionId, userName, pass);
    }

    public void logout(String connectionId) throws InvalidActionException {
        tradingSystem.logout(connectionId);
    }

    public Collection<String> getItems(String keyWord, String productName, String category, String subCategory,
                                       Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) {
        return tradingSystem.getItems(keyWord,productName,category,subCategory,ratingItem,ratingStore,maxPrice,minPrice);
    }

    public void addItemToBasket(String connectionId, String storeId, String productId, int quantity) throws InvalidActionException {
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).addItem(item, quantity);
    }

    public Collection<String> showCart(String connectionId) throws InvalidActionException {
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

    public Collection<String> showBasket(String connectionId, String storeId) throws InvalidActionException {
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Basket basket = tradingSystem.getUserByConnectionId(connectionId).getBasket(store);

        Set<Map.Entry<Item, Integer>> entries = basket.getItems().entrySet();
        Collection<String> items = new ArrayList<>(entries.size());

        for (Map.Entry<Item, Integer> entry : entries) {
            String name = entry.getKey().getName();
            Integer quantity = entry.getValue();
            items.add("Store: " + store.getName() + " Item: " + name + " Quantity: " + quantity);
        }
        return items;
    }

    public void updateProductAmountInBasket(String connectionId, String storeId, String productId, int quantity) throws InvalidActionException {
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).setQuantity(item, quantity);
    }

    public void purchaseCart(String connectionId) throws InvalidActionException {
        tradingSystem.purchaseCart(tradingSystem.getUserByConnectionId(connectionId));
    }

    public Collection<String> getPurchaseHistory(String connectionId) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        return subscriber.getPurchaseHistory();
    }

    public void writeOpinionOnProduct(String connectionId, String storeId, String itemId, String review) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.writeOpinionOnProduct(store, Integer.parseInt(itemId), review);
    }

    public Collection<String> getStoresInfo(String connectionId) throws InvalidActionException {
        Collection<String> infoList = new LinkedList<>();
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        for (Store store : subscriber.getAllStores(tradingSystem.getStores()))
            infoList.add(store.toString());

        return infoList;
    }

    public Collection<String> getItemsByStore(String connectionId, String storeId) throws InvalidActionException {
        Collection<String> itemList = new LinkedList<>();
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        for (Item item : subscriber.getStoreItems(store))
            itemList.add(item.getName());

        return itemList;
    }

    public String openNewStore(String connectionId, String newStoreName) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        return "" + tradingSystem.newStore(subscriber, newStoreName);
    }

    public void appointStoreManager(String connectionId, String targetUserName, String storeId)
            throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addManagerPermission(target, store);
    }

    public String addProductToStore(String connectionId, String storeId, String itemName, String category, String subCategory, int quantity, double price)
            throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        return "" + subscriber.addStoreItem(store, itemName, category, subCategory, quantity, price);
    }

    public void deleteProductFromStore(String connectionId, String storeId, String itemId) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeStoreItem(store, Integer.parseInt(itemId));
    }

    public void updateProductDetails(String connectionId, String storeId, String itemId, String newSubCategory, Integer newQuantity, Double newPrice)
            throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.updateStoreItem(store, Integer.parseInt(itemId), newSubCategory, newQuantity, newPrice);
    }

    public void appointStoreOwner(String connectionId, String targetUserName, String storeId) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addOwnerPermissions(target, store);
    }

    public void allowManagerToUpdateProducts(String connectionId, String storeId, String targetUserName) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addInventoryManagementPermission(target, store);
    }

    public void disableManagerFromUpdateProducts(String connectionId, String storeId, String targetUserName) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeInventoryManagementPermission(target, store);
    }

    public void allowManagerToEditPolicies(String connectionId, String storeId, String managerUserName) {
    }

    public void disableManagerFromEditPolicies(String connectionId, String storeId, String managerUserName) {
    }

    public void allowManagerToGetHistory(String connectionId, String storeId, String targetUserName) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addGetHistoryPermission(target, store);
    }

    public void disableManagerFromGetHistory(String connectionId, String storeId, String targetUserName) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeGetHistoryPermission(target, store);
    }

    public boolean removeManager(String connectionId, String storeId, String targetUserName) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));

        if (!target.havePermission(ManagerPermission.getInstance(store)))
            return false;

        subscriber.removeManagerPermission(target, store);
        return true;
    }

    public Collection<String> showStaffInfo(String connectionId, String storeId) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Collection<Subscriber> staff = tradingSystem.getStoreStaff(subscriber, store, new LinkedList<>());
        Collection<String> staffList = new LinkedList<>();
        for (Subscriber staffMember : staff)
            staffList.add(staffMember.getUserName() + " : " + staffMember.storePermissionsToString(store));

        return staffList;
    }

    public Collection<String> getSalesHistoryByStore(String connectionId, String storeId) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));

        return subscriber.getSalesHistoryByStore(store);
    }

    public Collection<String> getEventLog(String connectionId, Collection<String> eventLog) throws InvalidActionException, IOException {
        return tradingSystem.getUserByConnectionId(connectionId).getSubscriber().getEventLog(eventLog);
    }

    public Collection<String> getErrorLog(String connectionId) {
        return null;
    }

}
