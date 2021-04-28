package service;

import Logger.EventLog;
import exceptions.InvalidActionException;
import org.apache.log4j.PropertyConfigurator;
import tradingSystem.TradingSystemImpl;

import java.io.IOException;
import java.util.Collection;

public class TradingSystemServiceImpl implements TradingSystemService {

    private final TradingSystemImpl tradingSystemImpl;
    private final EventLog eventLog;

    public TradingSystemServiceImpl(TradingSystemImpl tradingSystemImpl) {
        eventLog = new EventLog();
        PropertyConfigurator.configure("Dev/log4j.properties");

        this.tradingSystemImpl = tradingSystemImpl;
    }

    @Override
    public String connect() {
        eventLog.writeToLogger("New connection request");
        return tradingSystemImpl.connect();
    }

    @Override
    public void register(String userName, String password) throws InvalidActionException {
        eventLog.writeToLogger("Register with userName: " + userName + ", password: *********");
        tradingSystemImpl.register(userName, password);
    }

    @Override
    public void login(String connectID, String userName, String pass) throws InvalidActionException {
        eventLog.writeToLogger("Login with userName: " + userName + ", password: *********");
        tradingSystemImpl.login(connectID, userName, pass);
    }

    @Override
    public void logout(String connectID) throws InvalidActionException {
        eventLog.writeToLogger("Logout subscriber");
        tradingSystemImpl.logout(connectID);
    }

    @Override
    public Collection<String> getItems(String keyWord, String productName, String category, String subCategory, Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) throws InvalidActionException {
        eventLog.writeToLogger("Search for items with the attributes: " +
                "key word- " + keyWord +
                ", product name- " + productName +
                ", category- " + category +
                ", sub category- " + subCategory +
                ", rating item- " + ratingItem +
                ", rating store- " + ratingStore +
                ", max price- " + maxPrice +
                ", min price- " + minPrice);
        return tradingSystemImpl.getItems(keyWord, productName, category, subCategory, ratingItem, ratingStore, maxPrice, minPrice);
    }

    @Override
    public void addItemToBasket(String userID, String storeId, String productId, int amount) throws InvalidActionException {
        eventLog.writeToLogger("Add item to basket: store-" + storeId + ", product-" + productId + ", quantity- " + amount);
        tradingSystemImpl.addItemToBasket(userID, storeId, productId, amount);
    }

    @Override
    public Collection<String> showCart(String userID) throws InvalidActionException {
        eventLog.writeToLogger("Show user cart");
        return tradingSystemImpl.showCart(userID);
    }

    @Override
    public Collection<String> showBasket(String userID, String storeId) throws InvalidActionException {
        eventLog.writeToLogger("Show user basket for store: " + storeId);
        return tradingSystemImpl.showBasket(userID, storeId);
    }

    @Override
    public void updateProductAmountInBasket(String userID, String storeId, String productId, int newAmount) throws InvalidActionException {
        eventLog.writeToLogger("User update the amount of product-" + productId + " of the store-" + storeId + " with the new quantity-" + newAmount);
        tradingSystemImpl.updateProductAmountInBasket(userID, storeId, productId, newAmount);
    }

    public int newPolicy(String userID, String storeId) throws InvalidActionException { //creates empty policy
        eventLog.writeToLogger("User create a new policy of the store-" + storeId);
        return tradingSystemImpl.newPolicy(userID, storeId);
    }

    public void removePolicy(String userID, String storeId, int policy) throws InvalidActionException {
        eventLog.writeToLogger("User remove a policy of the store-" + storeId);
        tradingSystemImpl.removePolicy(userID, storeId, policy);
    }

    public void makeQuantityPolicy(String userID, String storeId, int policy, Collection<String> items, int minQuantity, int maxQuantity) throws InvalidActionException {
        eventLog.writeToLogger("User make quantity policy of the store-" + storeId);
        tradingSystemImpl.makeQuantityPolicy(userID, storeId, policy, items, minQuantity, maxQuantity);
    }

    public void makeBasketPurchasePolicy(String userID, String storeId, int policy, int minBasketValue) throws InvalidActionException {
        eventLog.writeToLogger("User make basket purchase policy of the store-" + storeId);
        tradingSystemImpl.makeBasketPurchasePolicy(userID, storeId, policy, minBasketValue);
    }

    public void makeTimePolicy(String userID, String storeId, int policy, Collection<String> items, String time) throws InvalidActionException {
        eventLog.writeToLogger("User make time policy of the store-" + storeId);
        tradingSystemImpl.makeTimePolicy(userID, storeId, policy, items, time);
    }

    public int andPolicy(String userID, String storeId, int policy1, int policy2) throws InvalidActionException {
        eventLog.writeToLogger("User make and policy of the store-" + storeId + "between policy: " + policy1 + " and policy: " + policy2);
        return tradingSystemImpl.andPolicy(userID, storeId, policy1, policy2);
    }

    public int orPolicy(String userID, String storeId, int policy1, int policy2) throws InvalidActionException {
        eventLog.writeToLogger("User make or policy of the store-" + storeId + "between policy: " + policy1 + " and policy: " + policy2);
        return tradingSystemImpl.orPolicy(userID, storeId, policy1, policy2);
    }

    public int xorPolicy(String userID, String storeId, int policy1, int policy2) throws InvalidActionException {
        eventLog.writeToLogger("User make xor policy of the store-" + storeId + "between policy: " + policy1 + " and policy: " + policy2);
        return tradingSystemImpl.xorPolicy(userID, storeId, policy1, policy2);
    }

    @Override
    public void purchaseCart(String userID) throws InvalidActionException {
        eventLog.writeToLogger("User purchase cart");
        tradingSystemImpl.purchaseCart(userID);
    }

    @Override
    public Collection<String> getPurchaseHistory(String userID) throws InvalidActionException {
        eventLog.writeToLogger("User ask for his purchase history");
        return tradingSystemImpl.getPurchaseHistory(userID);
    }

    @Override
    public void writeOpinionOnProduct(String userID, String storeID, String productId, String desc) throws InvalidActionException {
        eventLog.writeToLogger("User write opinion about an Item: " +
                "store- " + storeID + ", product- " + productId + ", description: " + desc);
        tradingSystemImpl.writeOpinionOnProduct(userID, storeID, productId, desc);
    }

    @Override
    public Collection<String> getStoresInfo(String userID) throws InvalidActionException {
        eventLog.writeToLogger("User ask for stores info");
        return tradingSystemImpl.getStoresInfo(userID);
    }

    @Override
    public Collection<String> getItemsByStore(String userID, String storeId) throws InvalidActionException {
        eventLog.writeToLogger("User ask for store: " + storeId + "info");
        return tradingSystemImpl.getItemsByStore(userID, storeId);
    }

    @Override
    public String openNewStore(String userID, String newStoreName) throws InvalidActionException {
        eventLog.writeToLogger("User open new store named: " + newStoreName);
        return tradingSystemImpl.openNewStore(userID, newStoreName);
    }

    @Override
    public void appointStoreManager(String userID, String assigneeUserName, String storeId) throws InvalidActionException {
        eventLog.writeToLogger("User appoint " + assigneeUserName + " for store: " + storeId + " manager");
        tradingSystemImpl.appointStoreManager(userID, assigneeUserName, storeId);
    }

    @Override
    public String addProductToStore(String userID, String storeId, String productName, String category, String subCategory, int quantity, double price) throws InvalidActionException {
        eventLog.writeToLogger("Add product to store: " + storeId +
                ", name- " + productName +
                ", category- " + category +
                ", sub category- " + subCategory +
                ", quantity- " + quantity +
                ", price- " + price);
        return tradingSystemImpl.addProductToStore(userID, storeId, productName, category, subCategory, quantity, price);
    }

    @Override
    public void deleteProductFromStore(String userID, String storeId, String productID) throws InvalidActionException {
        eventLog.writeToLogger("Delete product from store: " + storeId +
                ", item- " + productID);
        tradingSystemImpl.deleteProductFromStore(userID, storeId, productID);
    }

    @Override
    public void updateProductDetails(String userID, String storeId, String productID, String newSubCategory, Integer newQuantity, Double newPrice) throws InvalidActionException {
        eventLog.writeToLogger("Update item details from store: " + storeId +
                ", item- " + productID +
                ", sub category- " + newSubCategory +
                ", quantity- " + newQuantity +
                ", price- " + newPrice);
        tradingSystemImpl.updateProductDetails(userID, storeId, productID, newSubCategory, newQuantity, newPrice);
    }

    @Override
    public void appointStoreOwner(String userID, String assigneeUserName, String storeId) throws InvalidActionException {
        eventLog.writeToLogger("User appoint " + assigneeUserName + " for store: " + storeId + " owner");
        tradingSystemImpl.appointStoreOwner(userID, assigneeUserName, storeId);
    }

    @Override
    public void allowManagerToUpdateProducts(String userID, String storeId, String managerUserName) throws InvalidActionException {
        eventLog.writeToLogger("User allow " + managerUserName + " to update products for store: " + storeId);
        tradingSystemImpl.allowManagerToUpdateProducts(userID, storeId, managerUserName);
    }

    @Override
    public void disableManagerFromUpdateProducts(String userID, String storeId, String managerUserName) throws InvalidActionException {
        eventLog.writeToLogger("User disable " + managerUserName + " from update products for store: " + storeId);
        tradingSystemImpl.disableManagerFromUpdateProducts(userID, storeId, managerUserName);
    }

    @Override
    public void allowManagerToEditPolicies(String userID, String storeId, String managerUserName) {
        eventLog.writeToLogger("allow manager to edit policies");
        tradingSystemImpl.allowManagerToEditPolicies(userID, storeId, managerUserName);
    }

    @Override
    public void disableManagerFromEditPolicies(String userID, String storeId, String managerUserName) {
        eventLog.writeToLogger("disable manager to edit policies");
        tradingSystemImpl.disableManagerFromEditPolicies(userID, storeId, managerUserName);
    }

    @Override
    public void allowManagerToGetHistory(String userID, String storeId, String managerUserName) throws InvalidActionException {
        eventLog.writeToLogger("allow manager to get history");
        tradingSystemImpl.allowManagerToGetHistory(userID, storeId, managerUserName);
    }

    @Override
    public void disableManagerFromGetHistory(String userID, String storeId, String managerUserName) throws InvalidActionException {
        eventLog.writeToLogger("disable manager to get history");
        tradingSystemImpl.disableManagerFromGetHistory(userID, storeId, managerUserName);
    }

    @Override
    public boolean removeManager(String userID, String storeId, String managerUserName) throws InvalidActionException {
        eventLog.writeToLogger("User remove " + managerUserName + " from manage store: " + storeId);
        return tradingSystemImpl.removeManager(userID, storeId, managerUserName);
    }

    @Override
    public boolean removeOwner(String connectionId, String storeId, String targetUserName) throws InvalidActionException {

        eventLog.writeToLogger("User remove " + targetUserName + " from store owner position for store id: " + storeId);
        return tradingSystemImpl.removeOwner(connectionId, storeId, targetUserName);
    }

    @Override
    public Collection<String> showStaffInfo(String userID, String storeId) throws InvalidActionException {
        eventLog.writeToLogger("Show staff info of store: " + storeId);
        return tradingSystemImpl.showStaffInfo(userID, storeId);
    }

    @Override
    public Collection<String> getSalesHistoryByStore(String userID, String storeId) throws InvalidActionException {
        eventLog.writeToLogger("Get sales history by store");
        return tradingSystemImpl.getSalesHistoryByStore(userID, storeId);
    }

    @Override
    public Collection<String> getEventLog(String userID) throws InvalidActionException, IOException {
        eventLog.writeToLogger("Get event log");
        Collection<String> eventLog = this.eventLog.getLog();
        return tradingSystemImpl.getEventLog(userID,eventLog);
    }

    @Override
    public Collection<String> getErrorLog(String userID) {
        eventLog.writeToLogger("Get error log");
        return tradingSystemImpl.getErrorLog(userID);
    }
}
