package acceptanceTests;

import exceptions.InvalidActionException;
import service.TradingSystemService;

import java.util.Collection;

public class TradingSystemServiceMock implements TradingSystemService {

    @Override
    public String connect() throws InvalidActionException {
        return null;
    }

    @Override
    public void register(String userName, String password) throws InvalidActionException {

    }

    @Override
    public void login(String userID, String userName, String pass) throws InvalidActionException {

    }

    @Override
    public void logout(String userID) throws InvalidActionException {

    }

    @Override
    public Collection<String> getItems(String keyWord, String productName, String category, String subCategory, Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) throws InvalidActionException {
        return null;
    }

    @Override
    public void addItemToBasket(String userID, String storeId, String productId, int amount) throws InvalidActionException {

    }

    @Override
    public Collection<String> showCart(String userID) throws InvalidActionException {
        return null;
    }

    @Override
    public Collection<String> showBasket(String userID, String storeId) throws InvalidActionException {
        return null;
    }

    @Override
    public void updateProductAmountInBasket(String userID, String storeId, String productId, int newAmount) throws InvalidActionException {

    }

    @Override
    public void purchaseCart(String userID) throws InvalidActionException {

    }

    @Override
    public Collection<String> getPurchaseHistory(String userID) throws InvalidActionException {
        return null;
    }

    @Override
    public void writeOpinionOnProduct(String userID, String storeID, String productId, String desc) throws InvalidActionException {

    }

    @Override
    public Collection<String> getStoresInfo(String userID) throws InvalidActionException {
        return null;
    }

    @Override
    public Collection<String> getItemsByStore(String userID, String storeId) throws InvalidActionException {
        return null;
    }

    @Override
    public String openNewStore(String userID, String newStoreName) throws InvalidActionException {
        return null;
    }

    @Override
    public void appointStoreManager(String userID, String assigneeUserName, String storeId) throws InvalidActionException {

    }

    @Override
    public String addProductToStore(String userID, String storeId, String productName, String category, String subCategory, int quantity, double price) throws InvalidActionException {
        return null;
    }

    @Override
    public void deleteProductFromStore(String userID, String storeId, String productID) throws InvalidActionException {

    }

    @Override
    public void updateProductDetails(String userID, String storeId, String productID, String newSubCategory, Integer newQuantity, Double newPrice) throws InvalidActionException {

    }

    @Override
    public void appointStoreOwner(String userID, String assigneeUserName, String storeId) throws InvalidActionException {

    }

    @Override
    public void allowManagerToUpdateProducts(String userID, String storeId, String managerUserName) throws InvalidActionException {

    }

    @Override
    public void disableManagerFromUpdateProducts(String userID, String storeId, String managerUserName) throws InvalidActionException {

    }

    @Override
    public void allowManagerToEditPolicies(String userID, String storeId, String managerUserName) throws InvalidActionException {

    }

    @Override
    public void disableManagerFromEditPolicies(String userID, String storeId, String managerUserName) throws InvalidActionException {

    }

    @Override
    public void allowManagerToGetHistory(String userID, String storeId, String managerUserName) throws InvalidActionException {

    }

    @Override
    public void disableManagerFromGetHistory(String userID, String storeId, String managerUserName) throws InvalidActionException {

    }

    @Override
    public boolean removeManager(String userID, String storeId, String managerUserName) throws InvalidActionException {
        return false;
    }

    @Override
    public boolean removeOwner(String connId, String storeId, String targetUserName) throws InvalidActionException {
        return false;
    }

    @Override
    public Collection<String> showStaffInfo(String userID, String storeId) throws InvalidActionException {
        return null;
    }

    @Override
    public Collection<String> getSalesHistoryByStore(String userID, String storeId) throws InvalidActionException {
        return null;
    }

    @Override
    public Collection<String> getEventLog(String userID) throws InvalidActionException {
        return null;
    }

    @Override
    public Collection<String> getErrorLog(String userID) throws InvalidActionException {
        return null;
    }
}
