package service;

import exceptions.*;
import authentication.UserAuthentication;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import tradingSystem.TradingSystem;
import user.Basket;

import java.util.Collection;
import java.util.HashMap;

public class TradingSystemService {

    private final PaymentSystem paymentSystem = new PaymentSystem();
    private final DeliverySystem deliverySystem = new DeliverySystem();
    private final UserAuthentication auth = new UserAuthentication(new HashMap<>());
    private TradingSystem tradingSystem;

    public void initializeSystem(String userName, String password) throws Exception {
        tradingSystem = new TradingSystem(userName, password, paymentSystem, deliverySystem, auth, new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    /**
     * Creates a new connection
     *
     * @return the connection
     */
    public String connect() {
        return tradingSystem.connect();
    }

    /**
     * Registers a new subscriber
     *
     * @param userName a unique user name
     * @param password password
     * @throws SubscriberAlreadyExistsException if the user name already exists
     */
    public void register(String userName, String password) throws SubscriberAlreadyExistsException {
        auth.register(userName, password);
    }

    /**
     * Connects the user's saved data with the connection
     *
     * @param connectionId the connection identifier
     * @param userName user name credential
     * @param password password credential
     * @throws LoginException if the login failed (the cause field holds the specific cause of failure)
     */
    public void login(String connectionId, String userName, String password) throws LoginException {
        tradingSystem.login(connectionId, userName, password);
    }

    public void logout(String connectionId) throws ConnectionIdDoesNotExistException {
        tradingSystem.logout(connectionId);
    }

    public Collection<String> getItems(String keyword, String productName, String category, String subCategory,
                                       double minItemRating, double minStoreRating, double minPrice, double maxPrice) {
        return null; // TODO
    }

    public void addItemToBasket(String connectionId, String storeId, String productId, int amount) throws ConnectionIdDoesNotExistException {
        Basket.ItemRecord itemRecord = new Basket.ItemRecord(productId, amount); // TODO
        tradingSystem.getUserByConnectionId(connectionId).getBasket(tradingSystem.getStore(storeId)).addItem(itemRecord);
    }

    public Collection<String> showCart(String userID) throws Exception {
        return null;
    }

    public String showBasket(String userID, String storeId) throws Exception {
        return null;
    }

    public void updateProductAmountInBasket(String userID, String storeId, String productId, int newAmount) throws Exception {

    }

    public void purchaseCart(String userID) throws Exception {

    }

    public Collection<String> getPurchaseHistory(String userID) throws Exception {
        return null;
    }

    public void writeOpinionOnProduct(String userID, String storeID, String productId, String desc) throws Exception {

    }

    public Collection<String> getStoresInfo(String userID) throws Exception {
        return null;
    }

    public Collection<String> getItemsByStore(String userID, String storeId) throws Exception {
        return null;
    }

    public String openNewStore(String userID, String newStoreName) throws Exception {
        return null;
    }

    public void appointStoreManager(String userID, String assigneeUserName, String storeId) throws Exception {

    }

    public String addProductToStore(String userID, String storeId, String productName, String category,
                                    String subCategory, int quantity, double price) throws Exception {

        //AddStoreItemCommand command = new AddStoreItemCommand(); // TODO
        //command.execute();
        return null; // TODO
    }

    public void deleteProductFromStore(String userID, String storeId, String productID) throws Exception {

    }

    public void updateProductDetails(String userID, String storeId, String productID, String newSubCategory, Integer newQuantity, Double newPrice) throws Exception {

    }

    public void appointStoreOwner(String userID, String assigneeUserName, String storeId) throws Exception {

    }

    public void allowManagerToUpdateProducts(String userID, String storeId, String managerUserName) throws Exception {

    }

    public void disableManagerFromUpdateProducts(String userID, String storeId, String managerUserName) throws Exception {

    }

    public void allowManagerToEditPolicies(String userID, String storeId, String managerUserName) throws Exception {

    }

    public void disableManagerFromEditPolicies(String userID, String storeId, String managerUserName) throws Exception {

    }

    public void allowManagerToGetHistory(String userID, String storeId, String managerUserName) throws Exception {

    }

    public void disableManagerFromGetHistory(String userID, String storeId, String managerUserName) throws Exception {

    }

    public void removeManager(String userID, String storeId, String managerUserName) throws Exception {

    }

    public Collection<String> showStaffInfo(String userID, String storeId) throws Exception {
        return null;
    }

    public Collection<String> getSalesHistoryByStore(String userID, String storeId) throws Exception {
        return null;
    }

    public Collection<String> getEventLog(String userID) throws Exception {
        return null;
    }

    public Collection<String> getErrorLog(String userID) throws Exception {
        return null;
    }
}
