package acceptanceTests;

import service.TradingSystemService;

import java.util.Collection;

public class ServiceProxy implements TradingSystemService {
    private TradingSystemService real;

    public void setReal(TradingSystemService real) {
        this.real = real;
    }
    @Override
    public void initializeSystem(String userName, String pass) throws Exception {

    }

    @Override
    public String connectGuest() throws Exception {
        return null;
    }

    @Override
    public void register(String userName, String password) throws Exception {

    }

    @Override
    public void login(String userID, String userName, String pass) throws Exception {

    }

    @Override
    public void logout(String userID) throws Exception {

    }

    @Override
    public Collection<String> getItems(String keyWord, String productName, String category, String subCategory, Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) throws Exception {
        return null;
    }

    @Override
    public void addItemToBasket(String userID, String storeId, String productId, int amount) throws Exception {

    }

    @Override
    public Collection<String> showCart(String userID) throws Exception {
        return null;
    }

    @Override
    public String showBasket(String userID, String storeId) throws Exception {
        return null;
    }

    @Override
    public void updateProductAmountInBasket(String userID, String storeId, String productId, int newAmount) throws Exception {

    }

    @Override
    public void purchaseCart(String userID) throws Exception {

    }

    @Override
    public Collection<String> getPurchaseHistory(String userID) throws Exception {
        return null;
    }

    @Override
    public void writeOpinionOnProduct(String userID, String storeID, String productId, String desc) throws Exception {

    }

    @Override
    public Collection<String> getStoresInfo(String userID) throws Exception {
        return null;
    }

    @Override
    public Collection<String> getItemsByStore(String userID, String storeId) throws Exception {
        return null;
    }

    @Override
    public String openNewStore(String userID, String newStoreName) throws Exception {
        return null;
    }

    @Override
    public void appointStoreManager(String userID, String assigneeUserName, String storeId) throws Exception {

    }

    @Override
    public String addProductToStore(String userID, String storeId, String productName, String category, String subCategory, int quantity, double price) throws Exception {
        return null;
    }

    @Override
    public void deleteProductFromStore(String userID, String storeId, String productID) throws Exception {

    }

    @Override
    public void updateProductDetails(String userID, String storeId, String productID, String newSubCategory, Integer newQuantity, Double newPrice) throws Exception {

    }

    @Override
    public void appointStoreOwner(String userID, String assigneeUserName, String storeId) throws Exception {

    }

    @Override
    public void allowManagerToUpdateProducts(String userID, String storeId, String managerUserName) throws Exception {

    }

    @Override
    public void disableManagerFromUpdateProducts(String userID, String storeId, String managerUserName) throws Exception {

    }

    @Override
    public void allowManagerToEditPolicies(String userID, String storeId, String managerUserName) throws Exception {

    }

    @Override
    public void disableManagerFromEditPolicies(String userID, String storeId, String managerUserName) throws Exception {

    }

    @Override
    public void allowManagerToGetHistory(String userID, String storeId, String managerUserName) throws Exception {

    }

    @Override
    public void disableManagerFromGetHistory(String userID, String storeId, String managerUserName) throws Exception {

    }

    @Override
    public void removeManager(String userID, String storeId, String managerUserName) throws Exception {

    }

    @Override
    public Collection<String> showStaffInfo(String userID, String storeId) throws Exception {
        return null;
    }

    @Override
    public Collection<String> getSalesHistoryByStore(String userID, String storeId) throws Exception {
        return null;
    }

    @Override
    public Collection<String> getEventLog(String userID) throws Exception {
        return null;
    }

    @Override
    public Collection<String> getErrorLog(String userID) throws Exception {
        return null;
    }
}
