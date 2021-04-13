package service;

import exceptions.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;
import user.AdminPermission;
import user.Basket;
import user.ManagerPermission;
import user.Subscriber;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class TradingSystemServiceImpl implements TradingSystemService {

    private static final Logger logger = Logger.getLogger(TradingSystemServiceImpl.class);

    TradingSystem tradingSystem;

    public TradingSystemServiceImpl(TradingSystem tradingSystem) {

        PropertyConfigurator.configure("Dev/log4j.properties");

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

    @Override
    public String connect() {

        logger.info("New connection request");

        return tradingSystem.connect();
    }

    @Override
    public void register(String userName, String password) throws InvalidActionException {

        logger.info("Register with userName: " + userName + ", password: *********");

        tradingSystem.register(userName, password);
    }

    @Override
    public void login(String connectionId, String userName, String pass) throws InvalidActionException {

        logger.info("Login with userName: " + userName + ", password: *********");

        tradingSystem.login(connectionId, userName, pass);
    }

    @Override
    public void logout(String connectionId) throws InvalidActionException {

        logger.info("Logout subscriber");

        tradingSystem.logout(connectionId);
    }

    @Override
    public Collection<String> getItems(String keyWord, String productName, String category, String subCategory,
                                       Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) {

        logger.info("Search for items with the attributes: " +
                "key word- " + keyWord +
                ", product name- " + productName +
                ", category- " + category +
                ", sub category- " + subCategory +
                ", rating item- " + ratingItem +
                ", rating store- " + ratingStore +
                ", max price- " + maxPrice +
                ", min price- " + minPrice);

        return tradingSystem.getItems(keyWord,productName,category,subCategory,ratingItem,ratingStore,maxPrice,minPrice);
    }

    @Override
    public void addItemToBasket(String connectionId, String storeId, String productId, int quantity) throws InvalidActionException {

        logger.info("Add item to basket: store-" + storeId + ", product-" + productId
                + ", quantity- " + quantity);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).addItem(item, quantity);
    }

    @Override
    public Collection<String> showCart(String connectionId) throws InvalidActionException {

        logger.info("Show user cart");
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
    public Collection<String> showBasket(String connectionId, String storeId) throws InvalidActionException {

        logger.info("Show user basket for store: " + storeId);
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
    public void updateProductAmountInBasket(String connectionId, String storeId, String productId, int quantity) throws InvalidActionException {

        logger.info("User update the amount of product-" + productId + " of the store-" + storeId +
                    " with the new quantity-" + quantity);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).setQuantity(item, quantity);
    }
    @Override

    public void purchaseCart(String connectionId) throws InvalidActionException {
        logger.info("User purchase cart");

        tradingSystem.purchaseCart(tradingSystem.getUserByConnectionId(connectionId));
    }

    @Override
    public Collection<String> getPurchaseHistory(String connectionId) throws InvalidActionException {

        logger.info("User ask for his purchase history");
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        return subscriber.getPurchaseHistory();
    }

    @Override
    public void writeOpinionOnProduct(String connectionId, String storeId, String itemId, String review)
            throws InvalidActionException {

        logger.info("User write opinion about an Item: " +
                    "store- " + storeId + ", product- " + itemId + ", description: " + review);

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.writeOpinionOnProduct(store, Integer.parseInt(itemId), review);
    }

    @Override
    public Collection<String> getStoresInfo(String connectionId) throws InvalidActionException {

        logger.info("User ask for stores info");
        Collection<String> infoList = new LinkedList<>();
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        for (Store store : subscriber.getAllStores(tradingSystem.getStores()))
            infoList.add(store.toString());
        return infoList;
    }

    @Override
    public Collection<String> getItemsByStore(String connectionId, String storeId) throws InvalidActionException {

        logger.info("User ask for store: " + storeId + "info");
        Collection<String> itemList = new LinkedList<>();
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        for (Item item : subscriber.getStoreItems(store))
            itemList.add(item.getName());
        return itemList;
    }

    @Override
    public String openNewStore(String connectionId, String newStoreName) throws InvalidActionException {

        logger.info("User open new store named: " + newStoreName);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        return "" + tradingSystem.newStore(subscriber, newStoreName);
    }

    @Override
    public void appointStoreManager(String connectionId, String targetUserName, String storeId)
            throws InvalidActionException {

        logger.info("User appoint " + targetUserName + " for store: " + storeId + " manager");
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addManagerPermission(target, store);
    }

    @Override
    public String addProductToStore(String connectionId, String storeId, String itemName, String category, String subCategory, int quantity, double price)
            throws InvalidActionException {

        logger.info("Add product to store: " + storeId +
                ", name- " + itemName +
                ", category- " + category +
                ", sub category- " + subCategory +
                ", quantity- " + quantity +
                ", price- " + price);

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        return "" + subscriber.addStoreItem(store, itemName, category, subCategory, quantity, price);
    }

    @Override
    public void deleteProductFromStore(String connectionId, String storeId, String itemId)
            throws InvalidActionException {

        logger.info("Delete product from store: " + storeId +
                ", item- " + itemId);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeStoreItem(store, Integer.parseInt(itemId));
    }

    @Override
    public void updateProductDetails(String connectionId, String storeId, String itemId, String newSubCategory, Integer newQuantity, Double newPrice)
            throws InvalidActionException {

        logger.info("Update item details from store: " + storeId +
                ", item- " + itemId +
                ", sub category- " + newSubCategory +
                ", quantity- " + newQuantity +
                ", price- " + newPrice);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.updateStoreItem(store, Integer.parseInt(itemId), newSubCategory, newQuantity, newPrice);
    }

    @Override
    public void appointStoreOwner(String connectionId, String targetUserName, String storeId) throws InvalidActionException {

        logger.info("User appoint " + targetUserName + " for store: " + storeId + " owner");
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addOwnerPermission(target, store);
    }

    @Override
    public void allowManagerToUpdateProducts(String connectionId, String storeId, String targetUserName) throws InvalidActionException {

        logger.info("User allow " + targetUserName + " to update products for store: " + storeId);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addInventoryManagementPermission(target, store);
    }

    @Override
    public void disableManagerFromUpdateProducts(String connectionId, String storeId, String targetUserName) throws InvalidActionException {

        logger.info("User disable " + targetUserName + " from update products for store: " + storeId);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeInventoryManagementPermission(target, store);
    }

    @Override
    public void allowManagerToEditPolicies(String connectionId, String storeId, String managerUserName) {
        logger.info("allow manager to edit policies");
    }

    @Override
    public void disableManagerFromEditPolicies(String connectionId, String storeId, String managerUserName) {
        logger.info("disable manager to edit policies");
    }

    @Override
    public void allowManagerToGetHistory(String connectionId, String storeId, String managerUserName) {
        logger.info("allow manager to get history");
    }

    @Override
    public void disableManagerFromGetHistory(String connectionId, String storeId, String managerUserName) {
        logger.info("disable manager to get history");
    }

    @Override
    public boolean removeManager(String connectionId, String storeId, String targetUserName) throws InvalidActionException {

        logger.info("User remove " + targetUserName + " from manage store: " + storeId);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        if (!target.havePermission(ManagerPermission.getInstance(store)))
            return false;
        subscriber.removeManagerPermission(target, store);
        return true;
    }

    @Override
    public Collection<String> showStaffInfo(String connectionId, String storeId) throws InvalidActionException {

        logger.info("Show staff info of store: " + storeId);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Collection<Subscriber> staff = tradingSystem.getStoreStaff(subscriber, store, new LinkedList<>());
        Collection<String> staffList = new LinkedList<>();
        for (Subscriber staffMember : staff)
            staffList.add(staffMember.getUserName() + " : " + staffMember.storePermissionsToString(store));
        return staffList;
    }

    @Override
    public Collection<String> getSalesHistoryByStore(String connectionId, String storeId) throws InvalidActionException {

        // TODO should enable to add a permission to a manager to see the store history also
        logger.info("Get sales history by store");

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));

        return subscriber.getSalesHistoryByStore(store);
    }

    @Override
    public Collection<String> getEventLog(String connectionId) throws InvalidActionException, IOException {

        logger.info("Get event log");
        Collection<String> eventLog = new LinkedList<>();
        BufferedReader reader = new BufferedReader(new FileReader("Dev/logging.log"));
        //StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            eventLog.add(line);
           // stringBuilder.append(line);
           // stringBuilder.append(ls);
        }
// delete the last new line separator
        //stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();
        //String content = stringBuilder.toString();
        //System.out.println(content);

        return tradingSystem.getUserByConnectionId(connectionId).getSubscriber().getEventLog(eventLog);
    }

    @Override
    public Collection<String> getErrorLog(String connectionId) {
        logger.info("Get error log");

        return null;
    }

}
