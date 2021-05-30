package acceptanceTests;

import exceptions.InvalidActionException;
import exceptions.InvalidStoreIdException;
import service.TradingSystemService;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

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
    public void addItemToBasketByOffer(String userID, String storeId, String productId, int amount, double price) throws InvalidActionException {
        if(real != null)
            real.addItemToBasketByOffer(userID, storeId, productId, amount, price);
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
    public void purchaseCart(String userID, String card_number, int month, int year, String holder, String ccv, String id,
                             String name, String address, String city, String country, int zip) throws InvalidActionException {
        if(real != null)
            real.purchaseCart(userID, card_number, month, year, holder, ccv, id, name, address, city, country, zip);
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
    public Collection<String> getOffersByStore(String userID, String storeId) throws InvalidActionException {
        if(real != null)
            return real.getOffersByStore(userID, storeId);
        return null;
    }

    @Override
    public void approveOffer(String userID, String storeId, int offerID, Double price) throws InvalidActionException {
        if(real != null)
            real.approveOffer(userID, storeId, offerID, price);
    }

    @Override
    public String getTotalIncomeByStorePerDay(String userID, String storeId, String date) throws InvalidActionException {
        if(real != null)
            return real.getTotalIncomeByStorePerDay(userID, storeId, date);
        return null;
    }

    @Override
    public Collection<String> getTotalIncomeByAdminPerDay(String userID, String date) throws InvalidActionException {
        if(real != null)
            return real.getTotalIncomeByAdminPerDay(userID, date);
        return null;
    }

    @Override
    public Map<String, Integer> getTotalVisitorsByAdminPerDay(String userID, String date) throws InvalidActionException {
        if(real != null)
            return real.getTotalVisitorsByAdminPerDay(userID, date);
        return null;
    }

    @Override
    public Collection<Integer> getStorePolicies(String userID, String storeId) throws InvalidActionException {
        if(real != null)
            return real.getStorePolicies(userID, storeId);
        return null;
    }

    @Override
    public void assignStorePurchasePolicy(int policy, String userID, String storeId) throws InvalidActionException {
        if(real != null)
            real.assignStorePurchasePolicy(policy, userID, storeId);
    }

    @Override
    public void removePolicy(String userID, String storeId, int policy) throws InvalidActionException {
        if(real != null)
            real.removePolicy(userID, storeId, policy);
    }

    @Override
    public int makeQuantityPolicy(String userID, String storeId, Collection<String> items, int minQuantity, int maxQuantity) throws InvalidActionException {
        if(real != null)
            return real.makeQuantityPolicy(userID, storeId, items, minQuantity, maxQuantity);
        return -1;
    }

    @Override
    public int makeBasketPurchasePolicy(String userID, String storeId, int minBasketValue) throws InvalidActionException {
        if(real != null)
            return real.makeBasketPurchasePolicy(userID, storeId, minBasketValue);
        return -1;
    }

    @Override
    public int makeTimePolicy(String userID, String storeId, Collection<String> items, String time) throws InvalidActionException {
        if(real != null)
            return real.makeTimePolicy(userID, storeId, items, time);
        return -1;
    }

    @Override
    public int andPolicy(String userID, String storeId, int policy1, int policy2) throws InvalidActionException {
        if(real != null)
            return real.andPolicy(userID, storeId, policy1, policy2);
        return -1;
    }

    @Override
    public int orPolicy(String userID, String storeId, int policy1, int policy2) throws InvalidActionException {
        if(real != null)
            return real.orPolicy(userID, storeId, policy1, policy2);
        return -1;
    }

    @Override
    public int xorPolicy(String userID, String storeId, int policy1, int policy2) throws InvalidActionException {
        if(real != null)
            return real.xorPolicy(userID, storeId, policy1, policy2);
        return -1;
    }

    @Override
    public Collection<Integer> getStoreDiscounts(String userID, String storeId) throws InvalidActionException {
        if(real != null)
            return real.getStoreDiscounts(userID, storeId);
        return null;
    }

    @Override
    public void assignStoreDiscountPolicy(int discountId, String userID, String storeId) throws InvalidActionException {
        if(real != null)
            real.assignStoreDiscountPolicy(discountId, userID, storeId);
    }

    @Override
    public void removeDiscount(String userID, String storeId, int discountId) throws InvalidActionException {
        if(real != null)
            real.removeDiscount(userID, storeId, discountId);
    }

    @Override
    public int makeQuantityDiscount(String userID, String storeId, int discount, Collection<String> items, Integer policyId) throws InvalidActionException {
        if(real != null)
            return real.makeQuantityDiscount(userID, storeId, discount, items, policyId);
        return -1;
    }

    @Override
    public int makePlusDiscount(String userID, String storeId, int discountId1, int discountId2) throws InvalidActionException {
        if(real != null)
            return real.makePlusDiscount(userID, storeId, discountId1, discountId2);
        return -1;
    }

    @Override
    public int makeMaxDiscount(String userID, String storeId, int discountId1, int discountId2) throws InvalidActionException {
        if(real != null)
            return real.makeMaxDiscount(userID, storeId, discountId1, discountId2);
        return -1;
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

    @Override
    public void setStoreStatus(String storeId, boolean status) throws InvalidStoreIdException {

    }

    @Override
    public boolean isAdmin(String connectionId) throws InvalidActionException {
        if(real != null)
            return real.isAdmin(connectionId);
        return false;
    }

    @Override
    public Collection<String> getNotifications(String connectionID) throws InvalidActionException {
        if(real != null)
            return real.getNotifications(connectionID);
        return null;
    }
}
