package acceptanceTests;

import exceptions.InvalidActionException;
import service.TradingSystemService;

import java.io.IOException;
import java.util.Collection;

public class ServiceProxy implements TradingSystemService {
    private TradingSystemService real;

    public void setReal(TradingSystemService real) {
        this.real = real;
    }

    @Override
    public String connect() throws InvalidActionException {
        if(real != null)
            return real.connect();
        return null;
    }

    @Override
    public void register(String userName, String password) throws InvalidActionException {
        if(real != null)
            real.register(userName, password);
    }

    @Override
    public void login(String userID, String userName, String pass) throws InvalidActionException {
        if(real != null)
            real.login(userID, userName, pass);
    }

    @Override
    public void logout(String userID) throws InvalidActionException {
        if(real != null)
            real.logout(userID);
    }

    @Override
    public Collection<String> getItems(String keyWord, String productName, String category, String subCategory, Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) throws InvalidActionException {
        if(real != null)
            return real.getItems(keyWord, productName, category, subCategory, ratingItem, ratingStore, maxPrice, minPrice);
        return null;
    }

    @Override
    public void addItemToBasket(String userID, String storeId, String productId, int amount) throws InvalidActionException {
        if(real != null)
            real.addItemToBasket(userID, storeId, productId, amount);
    }

    @Override
    public Collection<String> showCart(String userID) throws InvalidActionException {
        if(real != null)
            return real.showCart(userID);
        return null;
    }

    @Override
    public Collection<String> showBasket(String userID, String storeId) throws InvalidActionException {
        if(real != null)
            return real.showBasket(userID, storeId);
        return null;
    }

    @Override
    public void updateProductAmountInBasket(String userID, String storeId, String productId, int newAmount) throws InvalidActionException {
        if(real != null)
            real.updateProductAmountInBasket(userID, storeId, productId, newAmount);
    }

    @Override
    public void purchaseCart(String userID) throws InvalidActionException {
        if(real != null)
            real.purchaseCart(userID);
    }

    @Override
    public Collection<String> getPurchaseHistory(String userID) throws InvalidActionException {
        if(real != null)
            return real.getPurchaseHistory(userID);
        return null;
    }

    @Override
    public void writeOpinionOnProduct(String userID, String storeID, String productId, String desc) throws InvalidActionException {
        if(real != null)
            real.writeOpinionOnProduct(userID, storeID, productId, desc);
    }

    @Override
    public Collection<String> getStoresInfo(String userID) throws InvalidActionException {
        if(real != null)
            return real.getStoresInfo(userID);
        return null;
    }

    @Override
    public Collection<String> getItemsByStore(String userID, String storeId) throws InvalidActionException {
        if(real != null)
            return real.getItemsByStore(userID, storeId);
        return null;
    }

    @Override
    public String openNewStore(String userID, String newStoreName) throws InvalidActionException {
        if(real != null)
            return real.openNewStore(userID, newStoreName);
        return null;
    }

    @Override
    public void appointStoreManager(String userID, String assigneeUserName, String storeId) throws InvalidActionException {
        if(real != null)
            real.appointStoreManager(userID, assigneeUserName, storeId);
    }

    @Override
    public String addProductToStore(String userID, String storeId, String productName, String category, String subCategory, int quantity, double price) throws InvalidActionException {
        if(real != null)
            return real.addProductToStore(userID, storeId, productName, category, subCategory, quantity, price);
        return null;
    }

    @Override
    public void deleteProductFromStore(String userID, String storeId, String productID) throws InvalidActionException {
        if(real != null)
            real.deleteProductFromStore(userID, storeId, productID);
    }

    @Override
    public void updateProductDetails(String userID, String storeId, String productID, String newSubCategory, Integer newQuantity, Double newPrice) throws InvalidActionException {
        if(real != null)
            real.updateProductDetails(userID, storeId, productID, newSubCategory, newQuantity, newPrice);
    }

    @Override
    public void appointStoreOwner(String userID, String assigneeUserName, String storeId) throws InvalidActionException {
        if(real != null)
            real.appointStoreOwner(userID, assigneeUserName, storeId);
    }

    @Override
    public void allowManagerToUpdateProducts(String userID, String storeId, String managerUserName) throws InvalidActionException {
        if(real != null)
            real.allowManagerToUpdateProducts(userID, storeId, managerUserName);
    }

    @Override
    public void disableManagerFromUpdateProducts(String userID, String storeId, String managerUserName) throws InvalidActionException {
        if(real != null)
            real.disableManagerFromUpdateProducts(userID, storeId, managerUserName);
    }

    @Override
    public void allowManagerToEditPolicies(String userID, String storeId, String managerUserName) throws InvalidActionException {
        if(real != null)
            real.allowManagerToEditPolicies(userID, storeId, managerUserName);
    }

    @Override
    public void disableManagerFromEditPolicies(String userID, String storeId, String managerUserName) throws InvalidActionException {
        if(real != null)
            real.disableManagerFromEditPolicies(userID, storeId, managerUserName);
    }

    @Override
    public void allowManagerToGetHistory(String userID, String storeId, String managerUserName) throws InvalidActionException {
        if(real != null)
            real.allowManagerToGetHistory(userID, storeId, managerUserName);
    }

    @Override
    public void disableManagerFromGetHistory(String userID, String storeId, String managerUserName) throws InvalidActionException {
        if(real != null)
            real.disableManagerFromGetHistory(userID, storeId, managerUserName);
    }

    @Override
    public boolean removeManager(String userID, String storeId, String managerUserName) throws InvalidActionException {
        if(real != null)
            return real.removeManager(userID, storeId, managerUserName);
        return false;
    }

    @Override
    public boolean removeOwner(String userID, String storeId, String targetUserName) throws InvalidActionException {
        if(real != null)
            return real.removeOwner(userID, storeId, targetUserName);
        return false;
    }

    @Override
    public Collection<String> showStaffInfo(String userID, String storeId) throws InvalidActionException {
        if(real != null)
            return real.showStaffInfo(userID, storeId);
        return null;
    }

    @Override
    public Collection<String> getSalesHistoryByStore(String userID, String storeId) throws InvalidActionException {
        if(real != null)
            return real.getSalesHistoryByStore(userID, storeId);
        return null;
    }

    @Override
    public Collection<String> getEventLog(String userID) throws InvalidActionException, IOException {
        if(real != null)
            return real.getEventLog(userID);
        return null;
    }

    @Override
    public Collection<String> getErrorLog(String userID) throws InvalidActionException {
        if(real != null)
            return real.getErrorLog(userID);
        return null;
    }
}
