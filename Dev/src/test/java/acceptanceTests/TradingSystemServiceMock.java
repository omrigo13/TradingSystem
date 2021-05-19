package acceptanceTests;

import exceptions.InvalidActionException;
import exceptions.InvalidStoreIdException;
import service.TradingSystemService;

import java.util.Collection;

public class TradingSystemServiceMock implements TradingSystemService {

    @Override
    public String connect() {
        return null;
    }

    @Override
    public void register(String userName, String password) {

    }

    @Override
    public void login(String userID, String userName, String pass) {

    }

    @Override
    public void logout(String userID) {

    }

    @Override
    public Collection<String> getItems(String keyWord, String productName, String category, String subCategory, Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) {
        return null;
    }

    @Override
    public void addItemToBasket(String userID, String storeId, String productId, int amount) {

    }

    @Override
    public Collection<String> showCart(String userID) {
        return null;
    }

    @Override
    public Collection<String> showBasket(String userID, String storeId) {
        return null;
    }

    @Override
    public void updateProductAmountInBasket(String userID, String storeId, String productId, int newAmount) {

    }

    @Override
    public void purchaseCart(String userID, String card_number, int month, int year, String holder, String ccv, String id,
                             String name, String address, String city, String country, int zip) {

    }

    @Override
    public Collection<String> getPurchaseHistory(String userID) {
        return null;
    }

    @Override
    public void writeOpinionOnProduct(String userID, String storeID, String productId, String desc) {

    }

    @Override
    public Collection<String> getStoresInfo(String userID) {
        return null;
    }

    @Override
    public Collection<String> getItemsByStore(String userID, String storeId) {
        return null;
    }

    @Override
    public String openNewStore(String userID, String newStoreName) {
        return null;
    }

    @Override
    public void appointStoreManager(String userID, String assigneeUserName, String storeId) {

    }

    @Override
    public String addProductToStore(String userID, String storeId, String productName, String category, String subCategory, int quantity, double price) {
        return null;
    }

    @Override
    public void deleteProductFromStore(String userID, String storeId, String productID) {

    }

    @Override
    public void updateProductDetails(String userID, String storeId, String productID, String newSubCategory, Integer newQuantity, Double newPrice) {

    }

    @Override
    public void appointStoreOwner(String userID, String assigneeUserName, String storeId) {

    }

    @Override
    public void allowManagerToUpdateProducts(String userID, String storeId, String managerUserName) {

    }

    @Override
    public void disableManagerFromUpdateProducts(String userID, String storeId, String managerUserName) {

    }

    @Override
    public void allowManagerToEditPolicies(String userID, String storeId, String managerUserName) {

    }

    @Override
    public void disableManagerFromEditPolicies(String userID, String storeId, String managerUserName) {

    }

    @Override
    public void allowManagerToGetHistory(String userID, String storeId, String managerUserName) {

    }

    @Override
    public void disableManagerFromGetHistory(String userID, String storeId, String managerUserName) {

    }

    @Override
    public boolean removeManager(String userID, String storeId, String managerUserName) {
        return false;
    }

    @Override
    public boolean removeOwner(String connId, String storeId, String targetUserName) {
        return false;
    }

    @Override
    public Collection<String> showStaffInfo(String userID, String storeId) {
        return null;
    }

    @Override
    public Collection<String> getSalesHistoryByStore(String userID, String storeId) {
        return null;
    }

    @Override
    public String getTotalIncomeByStorePerDay(String userID, String storeId, String date) {
        return null;
    }

    @Override
    public Collection<String> getTotalIncomeByAdminPerDay(String userID, String date) {
        return null;
    }

    @Override
    public Collection<Integer> getStorePolicies(String userID, String storeId) {
        return null;
    }

    @Override
    public void assignStorePurchasePolicy(int policy, String userID, String storeId) {
    }

    @Override
    public void removePolicy(String userID, String storeId, int policy) {

    }

    @Override
    public int makeQuantityPolicy(String userID, String storeId, Collection<String> items, int minQuantity, int maxQuantity) {
        return 0;
    }

    @Override
    public int makeBasketPurchasePolicy(String userID, String storeId, int minBasketValue) {
        return 0;
    }

    @Override
    public int makeTimePolicy(String userID, String storeId, Collection<String> items, String time) {
        return 0;
    }

    @Override
    public int andPolicy(String userID, String storeId, int policy1, int policy2) {
        return 0;
    }

    @Override
    public int orPolicy(String userID, String storeId, int policy1, int policy2) {
        return 0;
    }

    @Override
    public int xorPolicy(String userID, String storeId, int policy1, int policy2) {
        return 0;
    }

    @Override
    public Collection<Integer> getStoreDiscounts(String userID, String storeId) {
        return null;
    }

    @Override
    public void assignStoreDiscountPolicy(int discountId, String userID, String storeId) {

    }

    @Override
    public void removeDiscount(String userID, String storeId, int discountId) {

    }

    @Override
    public int makeQuantityDiscount(String userID, String storeId, int discount, Collection<String> items, Integer policyId) {
        return 0;
    }

    @Override
    public int makePlusDiscount(String userID, String storeId, int discountId1, int discountId2) {
        return 0;
    }

    @Override
    public int makeMaxDiscount(String userID, String storeId, int discountId1, int discountId2) {
        return 0;
    }

    @Override
    public Collection<String> getEventLog(String userID) {
        return null;
    }

    @Override
    public Collection<String> getErrorLog(String userID) {
        return null;
    }

    @Override
    public void setStoreStatus(String storeId, boolean status) throws InvalidStoreIdException {

    }
}
