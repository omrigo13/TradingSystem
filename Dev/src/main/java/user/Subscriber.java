package user;

import exceptions.*;
import purchaseAndReview.Review;
import store.Item;
import store.Store;

import java.util.*;

public class Subscriber extends User {

    private final String userName;
    private final Set<Permission> permissions;
    private final Map<Store, Collection<Item>> itemsPurchased;
    private final Collection<String> purchaseHistory;

    public Subscriber(String userName) {
        this(userName, new HashSet<>(), new HashMap<>(), new LinkedList<>());
    }

    Subscriber(String userName, Set<Permission> permissions, Map<Store, Collection<Item>> itemsPurchased, Collection<String> purchaseHistory) {
        this.userName = userName;
        this.permissions = permissions;
        this.itemsPurchased = itemsPurchased;
        this.purchaseHistory = purchaseHistory;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public Subscriber getSubscriber() {
        return this;
    }

    @Override
    public void addCartToPurchases(Map<Store, String> details) { // TODO unit test

        for (Map.Entry<Store, Basket> entry : baskets.entrySet()) {
            Store store = entry.getKey();
            Basket basket = entry.getValue();
            Collection<Item> itemsPurchasedFromStore = itemsPurchased.computeIfAbsent(store, k -> new HashSet<>());
            itemsPurchasedFromStore.addAll(basket.getItems().keySet());
        }

        // add each store purchase details string to the user's purchase history collection
        String cartPurchase = "";
        for (Map.Entry<Store, String> entry : details.entrySet())
            //purchaseHistory.add("Store: " + entry.getKey().getName() + "\n" + entry.getValue());
            cartPurchase += "Store: " + entry.getKey().getName() + "\n" + entry.getValue();
        purchaseHistory.add(cartPurchase);

        baskets.clear();
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        permissions.remove(permission);
    }

    public boolean havePermission(Permission permission) {
        return permissions.contains(permission);
    }

    public void validatePermission(Permission permission) throws NoPermissionException {
        if (!havePermission(permission))
            throw new NoPermissionException(permission.toString());
    }

    public void validateAtLeastOnePermission(Permission... permissions) throws NoPermissionException {
        for (Permission permission : permissions) {
            if (havePermission(permission))
                return;
        }
        throw new NoPermissionException(Arrays.toString(permissions));
    }

    public void addManagerPermission(Subscriber target, Store store) throws NoPermissionException, AlreadyManagerException {

        // check this user has the permission to perform this action
        validatePermission(OwnerPermission.getInstance(store));

        // check if the target is already a manager at this store
        Permission managerPermission = ManagerPermission.getInstance(store);
        if (target.havePermission(managerPermission))
            throw new AlreadyManagerException(userName);

        // add manager permission to the target
        target.addPermission(managerPermission);

        // give the user permission to delete the new permission that was added to the target
        addPermission(RemovePermissionPermission.getInstance(target, store));
    }

    public void removeManagerPermission(Subscriber target, Store store) throws NoPermissionException {

        // check this user has the permission to perform this action
        validatePermission(RemovePermissionPermission.getInstance(target, store));

        // TODO: what should be the behavior when the target is also an owner of the store

        removePermission(target, store, ManageInventoryPermission.getInstance(store));
        removePermission(target, store, ManagerPermission.getInstance(store));
    }

    public void addOwnerPermission(Subscriber target, Store store) throws NoPermissionException, AlreadyOwnerException {

        // check this user has the permission to perform this action
        Permission ownerPermission = OwnerPermission.getInstance(store);
        validatePermission(ownerPermission);

        // check if the target is already an owner at this store
        if (target.havePermission(ownerPermission))
            throw new AlreadyOwnerException(userName);

        // check if the target is a manager that was appointed by someone else
        ManagerPermission managerPermission = ManagerPermission.getInstance(store);
        if (target.havePermission(managerPermission))
            validatePermission(RemovePermissionPermission.getInstance(target, store));

        // at this point we know the target is not a manager at this store, or he is a manager appointed by the caller

        // add owner, manager and inventory management permissions to the target
        target.addPermission(ownerPermission);
        target.addPermission(managerPermission);
        target.addPermission(ManageInventoryPermission.getInstance(store));

        // give the user permission to delete the new permission that was added to the target
        addPermission(RemovePermissionPermission.getInstance(target, store));
    }

    public void removeOwnerPermission(Subscriber target, Store store) throws NoPermissionException {

        // check this user has the permission to perform this action
        validatePermission(RemovePermissionPermission.getInstance(target, store));

        removePermission(target, store, OwnerPermission.getInstance(store));
        removePermission(target, store, ManageInventoryPermission.getInstance(store));
        removePermission(target, store, ManagerPermission.getInstance(store));
    }

    public void addInventoryManagementPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsNotStoreManagerException {

        // check this user has the permission to perform this action
        validatePermission(OwnerPermission.getInstance(store));

        if (!target.havePermission(ManagerPermission.getInstance(store)))
            throw new TargetIsNotStoreManagerException(target.getUserName(), store.getName()); // TODO test

        // add the permission to the target (if he doesn't already have it)
        target.addPermission(ManageInventoryPermission.getInstance(store));
    }

    public void removeInventoryManagementPermission(Subscriber target, Store store) throws NoPermissionException {

        // check this user has the permission to perform this action
        validatePermission(RemovePermissionPermission.getInstance(target, store));

        // remove the permission from the target (if he has it)
        target.removePermission(ManageInventoryPermission.getInstance(store));
    }

    void removePermission(Subscriber target, Store store, Permission permission) throws NoPermissionException {

        // check this user has the permission to perform this action
        validatePermission(RemovePermissionPermission.getInstance(target, store));

        target.removePermission(permission);
    }

    public int addStoreItem(Store store, String itemName, String category, String subCategory, int quantity, double price)
            throws NoPermissionException, ItemException {

        // check this user has the permission to perform this action
        validatePermission(ManageInventoryPermission.getInstance(store));

        return store.addItem(itemName, price, category, subCategory, quantity);
    }

    public void removeStoreItem(Store store, int itemId) throws NoPermissionException, ItemException {

        // check this user has the permission to perform this action
        validatePermission(ManageInventoryPermission.getInstance(store));

        store.removeItem(itemId);
    }

    public void updateStoreItem(Store store, int itemId, String newSubCategory, Integer newQuantity, Double newPrice)
            throws NoPermissionException, ItemException {

        // check this user has the permission to perform this action
        validatePermission(ManageInventoryPermission.getInstance(store));

        store.changeItem(itemId,  newSubCategory, newQuantity, newPrice);
    }

    public Collection<Store> getAllStores(Collection<Store> stores) throws NoPermissionException {

        // check this user has the permission to perform this action
        validatePermission(AdminPermission.getInstance());

        return stores;
    }

    public Collection<Item> getStoreItems(Store store) throws NoPermissionException {

        // check this user has the permission to perform this action
        validateAtLeastOnePermission(AdminPermission.getInstance(), ManagerPermission.getInstance(store));

        return store.getItems().keySet();
    }

    public String storePermissionsToString(Store store) {

        String result = "";

        Permission ownerPermission = OwnerPermission.getInstance(store);
        Permission managerPermission = ManagerPermission.getInstance(store);
        Permission manageInventoryPermission = ManageInventoryPermission.getInstance(store);

        if (havePermission(ownerPermission))
            result += ownerPermission.toString() + " ";
        if (havePermission(managerPermission))
            result += managerPermission.toString() + " ";
        if (havePermission(manageInventoryPermission))
            result += manageInventoryPermission.toString() + " ";

        return result;
    }

    public Collection<String> getEventLog(Collection<String> log) throws NoPermissionException {

        // check this user has the permission to perform this action
        validatePermission(AdminPermission.getInstance());

        return log;
    }

    public Collection<String> getSalesHistoryByStore(Store store) throws NoPermissionException {

        validateAtLeastOnePermission(AdminPermission.getInstance(), ManagerPermission.getInstance(store));

        return store.getPurchaseHistory();
    }

    public Collection<String> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void writeOpinionOnProduct(Store store, int itemId, String review) throws ItemException, WrongReviewException {

        if (review == null || review.trim().isEmpty())
            throw new WrongReviewException("Review can't be empty or null");

        Item item = store.searchItemById(itemId);
        if (!itemsPurchased.get(store).contains(item))
            throw new ItemNotPurchasedException("Item ID: " + itemId + " item name: " + item.getName());

        item.addReview(new Review(this, store, item, review));
    }
}
