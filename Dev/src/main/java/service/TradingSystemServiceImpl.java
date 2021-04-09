package service;

import authentication.UserAuthentication;
import exceptions.*;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import org.apache.log4j.PropertyConfigurator;
import purchaseAndReview.Purchase;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;
import user.Basket;
import user.ManagerPermission;
import user.Subscriber;
import user.User;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.log4j.Logger;

public class TradingSystemServiceImpl implements TradingSystemService {

    TradingSystem tradingSystem;
    private static final Logger logger = Logger.getLogger(TradingSystemServiceImpl.class);
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
        PropertyConfigurator.configure("Dev/log4j.properties");
    }

    public TradingSystemServiceImpl(UserAuthentication userAuthentication) {
        this.auth = userAuthentication;
        this.paymentSystem = new PaymentSystem();
        this.deliverySystem = new DeliverySystem();
        this.subscribers = new HashMap<>();
        this.connections = new HashMap<>();
        this.stores = new HashMap<>();
        PropertyConfigurator.configure("Dev/log4j.properties");
    }

    @Override
    public void initializeSystem(String userName, String pass) throws SubscriberDoesNotExistException, WrongPasswordException {
        logger.info("Initialize system with userName: " + userName);
        tradingSystem = TradingSystem.createTradingSystem(userName, pass, paymentSystem, deliverySystem, auth,
                subscribers, connections, stores);
    }

    @Override
    public String connect() {
        logger.info("Connect to the system");
        return tradingSystem.connect();
    }

    @Override
    public void register(String userName, String password) throws SubscriberAlreadyExistsException {
        logger.info("Register with userName: " + userName + ", password:*********");

        tradingSystem.register(userName, password);
    }

    @Override
    public void login(String connectionId, String userName, String pass)
            throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, WrongPasswordException {
        logger.info("Login with userName: " + userName + ", password:*********");
        tradingSystem.login(connectionId, userName, pass);
    }

    @Override
    public void logout(String connectionId) throws ConnectionIdDoesNotExistException, NotLoggedInException {
        logger.info("Logout subscriber");
        tradingSystem.logout(connectionId, new User(new HashMap<>()));
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
    public void addItemToBasket(String connectionId, String storeId, String productId, int quantity) throws ConnectionIdDoesNotExistException, ItemException, InvalidStoreIdException {
        logger.info("Add item to basket: store-" + storeId + ", product-" + productId
                + ", quantity- " + quantity);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).addItem(item, quantity);
    }

    @Override
    public Collection<String> showCart(String connectionId) throws ConnectionIdDoesNotExistException {
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
    public Collection<String> showBasket(String connectionId, String storeId) throws ConnectionIdDoesNotExistException, InvalidStoreIdException {
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
    public void updateProductAmountInBasket(String connectionId, String storeId, String productId, int quantity) throws ConnectionIdDoesNotExistException, ItemException, InvalidStoreIdException {
        logger.info("User update the amount of product-" + productId + " of the store-" + storeId +
                    " with the new quantity-" + quantity);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).setQuantity(item, quantity);
    }
    @Override

    public void purchaseCart(String connectionId) throws ConnectionIdDoesNotExistException, ExternalServicesException, Exception {
         logger.info("User purchase cart");
         tradingSystem.purchaseCart(connectionId);
    }

    @Override
    public Collection<String> getPurchaseHistory(String connectionId) throws NotLoggedInException, ConnectionIdDoesNotExistException {
        logger.info("User ask for his purchase history");
        Subscriber user = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Collection<String> purchases = new LinkedList<>();
        for (Purchase purchase: user.getPurchases()) {
            purchases.add(purchase.getDetails());
        }
        return purchases;
    } //TODO add a permission to subscriber user to see his history

    @Override
    public void writeOpinionOnProduct(String connectionId, String storeID, String productId, String desc) throws ConnectionIdDoesNotExistException, ItemException, NotLoggedInException, WrongReviewException {
        logger.info("User write opinion about an Item: " +
                    "store- " + storeID + ", product- " + productId + ", description: " + desc);
        tradingSystem.writeOpinionOnProduct(connectionId, storeID, productId, desc);
    }

    @Override
    public Collection<String> getStoresInfo(String connectionId) throws ConnectionIdDoesNotExistException, NotLoggedInException, NoPermissionException {
        logger.info("User ask for stores info");
        Collection<String> infoList = new LinkedList<>();
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        for (Store store : subscriber.getAllStores(tradingSystem.getStores()))
            infoList.add(store.toString());
        return infoList;
    }

    @Override
    public Collection<String> getItemsByStore(String connectionId, String storeId) throws ConnectionIdDoesNotExistException, NotLoggedInException, NoPermissionException, InvalidStoreIdException {
        logger.info("User ask for store: " + storeId + "info");
        Collection<String> itemList = new LinkedList<>();
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        for (Item item : subscriber.getStoreItems(store))
            itemList.add(item.getName());
        return itemList;
    }

    @Override
    public String openNewStore(String connectionId, String newStoreName) throws ConnectionIdDoesNotExistException, NotLoggedInException, ItemException {
        logger.info("User open new store named: " + newStoreName);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        return "" + tradingSystem.newStore(subscriber, newStoreName);
    }

    @Override
    public void appointStoreManager(String connectionId, String targetUserName, String storeId)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException,
                NoPermissionException, AlreadyManagerException, InvalidStoreIdException {
        logger.info("User appoint " + targetUserName + " for store: " + storeId + " manager");
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addManagerPermission(target, store);
    }

    @Override
    public String addProductToStore(String connectionId, String storeId, String itemName, String category, String subCategory, int quantity, double price)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, AddStoreItemException, InvalidStoreIdException, ItemException {
        logger.info("Add product to store: " + storeId +
                ", name- " + itemName +
                ", category- " + category +
                ", sub category- " + subCategory +
                ", quantity- " + quantity +
                ", price- " + price);

         return tradingSystem.addProductToStore(connectionId,storeId,itemName,category,subCategory,quantity,price);
    }

    @Override
    public void deleteProductFromStore(String connectionId, String storeId, String itemId)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, RemoveStoreItemException, InvalidStoreIdException {
        logger.info("Delete product from store: " + storeId +
                ", item- " + itemId);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeStoreItem(store, Integer.parseInt(itemId));
    }

    @Override
    public void updateProductDetails(String connectionId, String storeId, String itemId, String newSubCategory, Integer newQuantity, Double newPrice)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, UpdateStoreItemException, InvalidStoreIdException {
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
    public void appointStoreOwner(String connectionId, String targetUserName, String storeId)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, InvalidStoreIdException, AlreadyOwnerException {
        logger.info("User appoint " + targetUserName + " for store: " + storeId + " owner");
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addOwnerPermission(target, store);
    }

    @Override
    public void allowManagerToUpdateProducts(String connectionId, String storeId, String targetUserName)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, TargetIsNotStoreManagerException, InvalidStoreIdException {
        logger.info("User allow " + targetUserName + " to update products for store: " + storeId);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addInventoryManagementPermission(target, store);
    }

    @Override
    public void disableManagerFromUpdateProducts(String connectionId, String storeId, String targetUserName)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, InvalidStoreIdException {
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
    public boolean removeManager(String connectionId, String storeId, String targetUserName)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, SubscriberDoesNotExistException, NoPermissionException, InvalidStoreIdException {
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
    public Collection<String> showStaffInfo(String connectionId, String storeId)
            throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException, InvalidStoreIdException {
        logger.info("Show staff info of store: " + storeId);
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Collection<Subscriber> staff = tradingSystem.getStoreStaff(subscriber, store, new LinkedList<>());
        Collection<String> staffList = new LinkedList<>();
        for (Subscriber staffMember : staff)
            staffList.add(staffMember.storePermissionsToString(store));
        return staffList;
    }

    @Override
    public Collection<String> getSalesHistoryByStore(String connectionId, String storeId) throws ConnectionIdDoesNotExistException, InvalidStoreIdException {
        //TODO admin permission to see all stores history new function to add use case 6.4
        //TODO store owner get a permission to see store purchase history and he can add a permission to a manager to see the store history also
        logger.info("Get sales history by store");
        Collection<String> purchases = new LinkedList<>();
        for (Purchase purchase: tradingSystem.getStore(Integer.parseInt(storeId)).getPurchases()) {
            purchases.add(purchase.getDetails());
        }
        return purchases;
    }

    @Override
    public Collection<String> getEventLog(String connectionId) throws IOException {
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
        return eventLog;
    }

    @Override
    public Collection<String> getErrorLog(String connectionId) {
        logger.info("Get error log");
        return null;
    }

}
