package tradingSystem;

import Offer.Offer;
import exceptions.InvalidActionException;
import exceptions.InvalidStoreIdException;
import exceptions.NoPermissionException;
import exceptions.NotLoggedInException;
import notifications.Notification;
import externalServices.DeliveryData;
import externalServices.PaymentData;
import persistence.Repo;
import store.Item;
import store.Store;
import user.*;

import javax.persistence.EntityTransaction;
import java.time.LocalTime;
import java.util.*;

public class TradingSystemImpl {

    TradingSystem tradingSystem;

    public TradingSystemImpl(TradingSystem tradingSystem) {

        this.tradingSystem = tradingSystem;
    }

    public String connect() {
        return tradingSystem.connect();
    }

    //DB - ok
    public void register(String userName, String password) throws InvalidActionException {

        tradingSystem.register(userName, password);
    }

    //DB - ok
    public void login(String connectionId, String userName, String pass) throws InvalidActionException {

        tradingSystem.login(connectionId, userName, pass);
    }

    //DB - ok
    public void logout(String connectionId) throws InvalidActionException {

        tradingSystem.logout(connectionId);
    }

    //DB - ok
    public Collection<String> getItems(String keyWord, String productName, String category, String subCategory,
                                       Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) {

        return tradingSystem.getItems(keyWord,productName,category,subCategory,ratingItem,ratingStore,maxPrice,minPrice);
    }

    //DB - ok
    public void addItemToBasket(String connectionId, String storeId, String productId, int quantity) throws InvalidActionException {

        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).addItem(item, quantity);

    }

    //DB - ok
    public void addItemToBasketByOffer(String connectionId, String storeId, String productId, int quantity, double price) throws InvalidActionException {

        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        store.addOffer(subscriber, item, quantity, price);
        for (Offer offer: store.getStoreOffers().values()) {
            if(offer.getSubscriber() == subscriber && offer.getItem() == item && offer.getQuantity() == quantity && offer.getPrice() == price) {
                store.notifyNewOffer(offer);
                break;
            }
        }
    }

    //DB - ok
    public Collection<String> showCart(String connectionId) throws InvalidActionException {

        Collection<String> itemList = new LinkedList<>();
        Map<Store, Basket> cart = tradingSystem.getUserByConnectionId(connectionId).getCart();
        for (Map.Entry<Store, Basket> storeBasketEntry : cart.entrySet()) {
            Store store = storeBasketEntry.getKey();
            String storeName = store.getName();
            Map<Item, Integer> items = storeBasketEntry.getValue().getItems();

            for (Map.Entry<Item, Integer> itemQuantityEntry : items.entrySet()) {
                Item item = itemQuantityEntry.getKey();
                Integer quantity = itemQuantityEntry.getValue();
                String itemString = null;
                try {
                    Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
                    Offer offer = store.searchOfferByItemAndSubscriber(subscriber, item);
                    if(offer != null && offer.isApproved())
                        itemString = "Store: " + storeName + " Item: " + item.getName() + " Quantity: " + offer.getQuantity() + " price: " + offer.getPrice();
                    else
                        itemString = "Store: " + storeName + " Item: " + item.getName() + " Quantity: " + quantity + " price: " + item.getPrice();
                }
                catch (NotLoggedInException e) {
                    itemString = "Store: " + storeName + " Item: " + item.getName() + " Quantity: " + quantity + " price: " + item.getPrice();
                }
                itemList.add(itemString);
            }
        }

        return itemList;
    }

    //DB - ok
    public Collection<String> showBasket(String connectionId, String storeId) throws InvalidActionException {

        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Basket basket = tradingSystem.getUserByConnectionId(connectionId).getBasket(store);

        Set<Map.Entry<Item, Integer>> entries = basket.getItems().entrySet();
        Collection<String> items = new ArrayList<>(entries.size());

        for (Map.Entry<Item, Integer> entry : entries) {
            String name = entry.getKey().getName();
            Integer quantity = entry.getValue();
            items.add("Store: " + store.getName() + " Item: " + name + " Quantity: " + quantity);
        }
        return items;
    }

    //DB - ok
    public void updateProductAmountInBasket(String connectionId, String storeId, String productId, int quantity) throws InvalidActionException {

        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Item item = store.searchItemById(Integer.parseInt(productId));
        tradingSystem.getUserByConnectionId(connectionId).getBasket(store).setQuantity(item, quantity);
    }

    //DB - ok
    public Collection<Integer> getStorePolicies(String connectionId, String storeId) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.getStorePolicies(store);
    }

    //DB - ok
    public void assignStorePurchasePolicy(int policy, String connectionId, String storeId) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        tradingSystem.assignStorePurchasePolicy(policy, store);
    }

    //DB - ok
    public void removePolicy(String connectionId, String storeId, int policy) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        tradingSystem.removePolicy(store, policy);
    }

    //DB - ok
    public int makeQuantityPolicy(String connectionId, String storeId, Collection<String> items, int minQuantity, int maxQuantity) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Collection<Item> policyItems = new ArrayList<>();
        for (String item: items) {
            policyItems.add(store.searchItemById(Integer.parseInt(item)));
        }
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.makeQuantityPolicy(store, policyItems, minQuantity, maxQuantity);
    }

    //DB - ok
    public int makeBasketPurchasePolicy(String connectionId, String storeId, int minBasketValue) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.makeBasketPurchasePolicy(store, minBasketValue);
    }

    //DB - ok
    public int makeTimePolicy(String connectionId, String storeId, Collection<String> items, String time) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Collection<Item> policyItems = new ArrayList<>();
        for (String item: items) {
            policyItems.add(store.searchItemById(Integer.parseInt(item)));
        }
        LocalTime policyTime = LocalTime.parse(time);
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.makeTimePolicy(store, policyItems, policyTime);
    }

    //DB - ok
    public int andPolicy(String connectionId, String storeId, int policy1, int policy2) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.andPolicy(store, policy1, policy2);
    }

    //DB - ok
    public int orPolicy(String connectionId, String storeId, int policy1, int policy2) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.orPolicy(store, policy1, policy2);
    }

    //DB - ok
    public int xorPolicy(String connectionId, String storeId, int policy1, int policy2) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.xorPolicy(store, policy1, policy2);
    }

    //DB - ok
    public Collection<Integer> getStoreDiscounts(String connectionId, String storeId) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.getStoreDiscounts(store);
    }

    //DB - ok
    public void assignStoreDiscountPolicy(int discountId, String connectionId, String storeId) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        tradingSystem.assignStoreDiscountPolicy(discountId, store);
    }

    //DB - ok
    public void removeDiscount(String connectionId, String storeId, int discountId) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        tradingSystem.removeDiscount(store, discountId);
    }

    //DB - ok
    public int makeQuantityDiscount(String connectionId, String storeId, int discount, Collection<String> items, Integer policyId) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Collection<Item> discountItems = new ArrayList<>();
        for (String item: items) {
            discountItems.add(store.searchItemById(Integer.parseInt(item)));
        }
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.makeQuantityDiscount(store, discount, discountItems, policyId);
    }

    //DB - ok
    public int makePlusDiscount(String connectionId, String storeId, int discountId1, int discountId2) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.makePlusDiscount(store, discountId1, discountId2);
    }

    //DB - ok
    public int makeMaxDiscount(String connectionId, String storeId, int discountId1, int discountId2) throws InvalidActionException {
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), EditPolicyPermission.getInstance(store));
        return tradingSystem.makeMaxDiscount(store, discountId1, discountId2);
    }

    //DB - ok
    public void purchaseCart(String connectionId, String card_number, int month, int year, String holder, String ccv, String id,
                             String name, String address, String city, String country, int zip) throws InvalidActionException {
        User user = tradingSystem.getUserByConnectionId(connectionId);
        PaymentData paymentData = new PaymentData(card_number, month, year, holder, ccv, id);
        DeliveryData deliveryData = new DeliveryData(name, address, city, country, zip);
        tradingSystem.purchaseCart(user, paymentData, deliveryData);
    }

    //todo - DB: take care of itemsPurchased
    public Collection<String> getPurchaseHistory(String connectionId) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        return subscriber.getPurchaseHistory();
    }

    //DB - ok
    public void writeOpinionOnProduct(String connectionId, String storeId, String itemId, String review) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.writeOpinionOnProduct(store, Integer.parseInt(itemId), review);
    }

    //DB - ok
    public Collection<String> getStoresInfo(String connectionId) throws InvalidActionException {

        Collection<String> infoList = new LinkedList<>();
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        for (Store store : subscriber.getAllStores(tradingSystem.getStores()))
            infoList.add(store.toString());

        return infoList;
    }

    //DB - ok
    public Collection<String> getItemsByStore(String connectionId, String storeId) throws InvalidActionException {

        Collection<String> itemList = new LinkedList<>();
        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        for (Item item : subscriber.getStoreItems(store).values())
            itemList.add("store: " + storeId + ", " + item.toString() + ", quantity: " + item.getAmount());

        return itemList;
    }

    //DB - ok
    public String openNewStore(String connectionId, String newStoreName) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        return "" + tradingSystem.newStore(subscriber, newStoreName);
    }

    //DB - ok
    public void appointStoreManager(String connectionId, String targetUserName, String storeId)
            throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addManagerPermission(target, store);
        store.appointRole(subscriber, target, "manager");
    }

    //DB - ok
    public String addProductToStore(String connectionId, String storeId, String itemName, String category, String subCategory, int quantity, double price)
            throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
//        return "" + subscriber.addStoreItem(store, itemName, category, subCategory, quantity, price);
        int itemId = subscriber.addStoreItem(store, itemName, category, subCategory, quantity, price);

        return "" + itemId;
    }

    //DB - ok
    public void deleteProductFromStore(String connectionId, String storeId, String itemId) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeStoreItem(store, Integer.parseInt(itemId));
    }

    //DB - ok
    public void updateProductDetails(String connectionId, String storeId, String itemId, String newSubCategory, Integer newQuantity, Double newPrice)
            throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.updateStoreItem(store, Integer.parseInt(itemId), newSubCategory, newQuantity, newPrice);
    }

    //DB - ok
    public void appointStoreOwner(String connectionId, String targetUserName, String storeId) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addOwnerPermission(target, store);

        store.appointRole(subscriber, target, "owner");
        store.subscribe(subscriber);
    }

    //DB - ok
    public void allowManagerToUpdateProducts(String connectionId, String storeId, String targetUserName) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addInventoryManagementPermission(target, store);
        Repo.merge(target);
    }

    //DB - ok
    public void disableManagerFromUpdateProducts(String connectionId, String storeId, String targetUserName) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeInventoryManagementPermission(target, store);
        Repo.merge(target);

    }

    //DB - ok
    public void allowManagerToEditPolicies(String connectionId, String storeId, String managerUserName) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(managerUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addEditPolicyPermission(target, store);
        Repo.merge(target);
    }

    //DB - ok
    public void disableManagerFromEditPolicies(String connectionId, String storeId, String managerUserName) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(managerUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeEditPolicyPermission(target, store);
        Repo.merge(target);

    }

    //DB - ok
    public void allowManagerToGetHistory(String connectionId, String storeId, String targetUserName) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.addGetHistoryPermission(target, store);
        Repo.merge(target);
    }

    //DB - ok
    public void disableManagerFromGetHistory(String connectionId, String storeId, String targetUserName) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        subscriber.removeGetHistoryPermission(target, store);
        Repo.merge(target);

    }

    //DB - ok
    public boolean removeManager(String connectionId, String storeId, String targetUserName) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));

        if (!target.havePermission(ManagerPermission.getInstance(store)))
            return false;

        subscriber.removeManagerPermission(target, store);
        store.removeOwnerOrManager(subscriber, target);

        Repo.merge(target);
        Repo.merge(store);

        return true;
    }

    //DB - ok
    public boolean removeOwner(String connectionId, String storeId, String targetUserName) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Subscriber target = tradingSystem.getSubscriberByUserName(targetUserName);
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));

        if (!target.havePermission(OwnerPermission.getInstance(store)))
            return false;

        subscriber.removeOwnerPermission(target, store);
        store.removeOwnerOrManager(subscriber, target);
        return true;
    }

    //DB - ok
    public Collection<String> showStaffInfo(String connectionId, String storeId) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        Collection<Subscriber> staff = tradingSystem.getStoreStaff(subscriber, store, new LinkedList<>());
        Collection<String> staffList = new LinkedList<>();
        for (Subscriber staffMember : staff)
            staffList.add(staffMember.getUserName() + " : " + staffMember.storePermissionsToString(store));

        return staffList;
    }

    //DB - ok
    public Collection<String> getSalesHistoryByStore(String connectionId, String storeId) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));

        return subscriber.getSalesHistoryByStore(store);
    }

    //DB - ok
    public Collection<String> getOffersByStore(String connectionId, String storeId) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));

        return subscriber.getOffersByStore(store);
    }

    //DB - ok
    public void approveOffer(String connectionId, String storeId, int offerId, double price) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        int storeOwners = tradingSystem.getStoreOwners(store);
        subscriber.approveOffer(store, offerId, price, storeOwners);
    }

    //DB - ok
    public String getTotalIncomeByStorePerDay(String connectionId, String storeId, String date) throws InvalidActionException {

        Subscriber subscriber = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        return subscriber.getTotalIncomeByStorePerDay(store, date);
    }

    //DB - ok
    public Collection<String> getTotalIncomeByAdminPerDay(String connectionId, String date) throws InvalidActionException {

        Subscriber admin = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        return tradingSystem.getTotalIncomeByAdminPerDay(admin, date);
    }

    //DB - ok
    public Map<String, Integer> getTotalVisitorsByAdminPerDay(String connectionId, String date) throws InvalidActionException {

        Subscriber admin = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        return tradingSystem.getTotalVisitorsByAdminPerDay(admin, date);
    }

    //DB - ok
    public Collection<String> getEventLog(String connectionId, Collection<String> eventLog) throws InvalidActionException {
        return tradingSystem.getUserByConnectionId(connectionId).getSubscriber().getEventLog(eventLog);
    }

    public Collection<String> getErrorLog(String connectionId) {
        return null;
    }

    //DB - ok
    public void setStoreStatus(String storeId, boolean status) throws InvalidStoreIdException {
        Store store = tradingSystem.getStore(Integer.parseInt(storeId));
        if(status == true)
            store.setActive();
        else
            store.setNotActive();
        Repo.merge(store);
    }

    //DB - ok
    public boolean isAdmin(String connectionId) throws InvalidActionException {
        Subscriber admin = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        try{
            admin.validatePermission(AdminPermission.getInstance());
            return true;
        }catch (NoPermissionException e){
            return false;
        }
    }

    //DB - ok
    public Collection<String> getNotifications(String connectionId) throws InvalidActionException {
        Subscriber user = tradingSystem.getUserByConnectionId(connectionId).getSubscriber();
        Collection<Notification> n1 = user.checkPendingNotifications();
        Collection<String> result = new LinkedList<>();
        for (Notification n: n1) {
            result.add(n.print());
        }
        return result;
    }
}
