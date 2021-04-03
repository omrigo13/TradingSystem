package service;

import authentication.UserAuthentication;
import exceptions.ConnectionIdDoesNotExistException;
import exceptions.LoginException;
import exceptions.SubscriberAlreadyExistsException;
import exceptions.SubscriberDoesNotExistException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;

import java.util.Collection;
import java.util.HashMap;

public class TradingSystemServiceImpl implements TradingSystemService {

    UserAuthentication auth;
    TradingSystem tradingSystem;

    @Override
    public void initializeSystem(String userName, String pass) throws LoginException {
        UserAuthentication auth = new UserAuthentication(new HashMap<>());
        tradingSystem = new TradingSystem(userName, pass, new PaymentSystem(), new DeliverySystem(), auth,
                new HashMap<>(), new HashMap<>(), new HashMap<>());
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
        return null;
    }

    @Override
    public void addItemToBasket(String connectionId, String storeId, String productId, int quantity) throws ConnectionIdDoesNotExistException {
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = null; // TODO store.getItem( ... )
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).addItem(item, quantity);
    }

    @Override
    public Collection<String> showCart(String connectionId) {
        return null; // TODO tradingSystem.getUserByConnectionId(connectionId).getCart().what?;
    }

    @Override
    public String showBasket(String connectionId, String storeId) throws ConnectionIdDoesNotExistException {
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        return tradingSystem.getUserByConnectionId(connectionId).getBasket(store).toString();
    }

    @Override
    public void updateProductAmountInBasket(String connectionId, String storeId, String productId, int quantity) throws ConnectionIdDoesNotExistException {
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = null; // TODO store.getItem( ... )
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).setQuantity(item, quantity);
    }
    @Override
    public void purchaseCart(String connectionId) {
    }

    @Override
    public Collection<String> getPurchaseHistory(String connectionId) {
        return null;
    }

    @Override
    public void writeOpinionOnProduct(String connectionId, String storeID, String productId, String desc) {
    }

    @Override
    public Collection<String> getStoresInfo(String connectionId) {
        return null;
    }

    @Override
    public Collection<String> getItemsByStore(String connectionId, String storeId) {
        return null;
    }

    @Override
    public String openNewStore(String connectionId, String newStoreName) {
        return null;
    }

    @Override
    public void appointStoreManager(String connectionId, String assigneeUserName, String storeId) {
    }

    @Override
    public String addProductToStore(String connectionId, String storeId, String productName, String category, String subCategory, int quantity, double price) {
        return null;
    }

    @Override
    public void deleteProductFromStore(String connectionId, String storeId, String productID) {

    }

    @Override
    public void updateProductDetails(String connectionId, String storeId, String productID, String newSubCategory, Integer newQuantity, Double newPrice) {

    }

    @Override
    public void appointStoreOwner(String connectionId, String assigneeUserName, String storeId) {

    }

    @Override
    public void allowManagerToUpdateProducts(String connectionId, String storeId, String managerUserName) {

    }

    @Override
    public void disableManagerFromUpdateProducts(String connectionId, String storeId, String managerUserName) {

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
    public boolean removeManager(String connectionId, String storeId, String managerUserName) {
        return false;
    }

    @Override
    public Collection<String> showStaffInfo(String connectionId, String storeId) {
        return null;
    }

    @Override
    public Collection<String> getSalesHistoryByStore(String connectionId, String storeId) {
        return null;
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
