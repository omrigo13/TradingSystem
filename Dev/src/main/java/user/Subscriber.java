package user;

import exceptions.*;
import review.Review;
import store.Item;
import store.Store;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Subscriber extends User {

    private final int id;
    private final String userName;
    private final Set<Permission> permissions; // synchronized manually
    private final ConcurrentHashMap<Store, Collection<Item>> itemsPurchased;
    private final Collection<String> purchaseHistory; // synchronized in constructor

    public Subscriber(int id, String userName) {
        this(id, userName, new HashSet<>(), new ConcurrentHashMap<>(), new LinkedList<>());
    }

    Subscriber(int id, String userName, Set<Permission> permissions, ConcurrentHashMap<Store, Collection<Item>> itemsPurchased, Collection<String> purchaseHistory) {
        this.id = id;
        this.userName = userName;
        this.permissions = permissions;
        this.itemsPurchased = itemsPurchased;
        this.purchaseHistory = Collections.synchronizedCollection(purchaseHistory);
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

    }

    public void addPermission(Permission permission) {

        synchronized (permissions) {
            permissions.add(permission);
        }
    }

    public void removePermission(Permission permission) {

        synchronized (permissions) {
            permissions.remove(permission);
        }
    }

    public boolean havePermission(Permission permission) {

        synchronized (permissions) {
            return permissions.contains(permission);
        }
    }

    public void validatePermission(Permission permission) throws NoPermissionException {

        synchronized (permissions) {
            if (!havePermission(permission))
                throw new NoPermissionException(permission.toString());
        }
    }

    public void validateAtLeastOnePermission(Permission... permissions) throws NoPermissionException {

        synchronized (this.permissions) {
            for (Permission permission : permissions) {
                if (havePermission(permission))
                    return;
            }
            throw new NoPermissionException(Arrays.toString(permissions));
        }
    }

    public void addManagerPermission(Subscriber target, Store store) throws NoPermissionException, AlreadyManagerException {

        synchronized (target.id < id ? target.permissions : permissions) {
            synchronized (target.id < id ? permissions : target.permissions) {

                // check this user has the permission to perform this action
                validatePermission(OwnerPermission.getInstance(store));

                // check if the target is already a manager at this store
                Permission managerPermission = ManagerPermission.getInstance(store);
                if (target.havePermission(managerPermission))
                    throw new AlreadyManagerException(userName);

                // add manager permission to the target
                target.addPermission(managerPermission);

                // give the user permission to delete the new permission that was added to the target
                addPermission(AppointerPermission.getInstance(target, store));
            }
        }
    }

    public void removeManagerPermission(Subscriber target, Store store) throws NoPermissionException {

        removeOwnerPermission(target, store); // removes all store permissions
    }

    public void addOwnerPermission(Store store) {

        synchronized (permissions) {

            addPermission(OwnerPermission.getInstance(store));
            addPermission(ManagerPermission.getInstance(store));
            addPermission(ManageInventoryPermission.getInstance(store));
            addPermission(GetHistoryPermission.getInstance(store));
        }
    }

    public void addOwnerPermission(Subscriber target, Store store) throws NoPermissionException, AlreadyOwnerException {

        synchronized (target.id < id ? target.permissions : permissions) {
            synchronized (target.id < id ? permissions : target.permissions) {

                // check this user has the permission to perform this action
                Permission ownerPermission = OwnerPermission.getInstance(store);
                validatePermission(ownerPermission);

                // check if the target is already an owner at this store
                if (target.havePermission(ownerPermission))
                    throw new AlreadyOwnerException(userName);

                // check if the target is a manager that was appointed by someone else
                ManagerPermission managerPermission = ManagerPermission.getInstance(store);
                if (target.havePermission(managerPermission))
                    validatePermission(AppointerPermission.getInstance(target, store));

                target.addOwnerPermission(store);

                // give the user permission to delete the new permission that was added to the target
                addPermission(AppointerPermission.getInstance(target, store));
            }
        }
    }

    public void removeOwnerPermission(Store store) {

        synchronized (permissions) {

            // look for any managers or owners that were appointed by this owner for this store
            for (Permission permission : permissions)
                if (permission.getClass() == AppointerPermission.class && ((AppointerPermission)permission).getStore() == store) {
                    Subscriber target = ((AppointerPermission)permission).getTarget();
                    target.removeOwnerPermission(store);

                    // remove this user's permission to change the target's permissions
                    removePermission(AppointerPermission.getInstance(target, store));
                }

            removePermission(OwnerPermission.getInstance(store));
            removePermission(ManageInventoryPermission.getInstance(store));
            removePermission(GetHistoryPermission.getInstance(store));
            removePermission(ManagerPermission.getInstance(store));
        }
    }

    public void removeOwnerPermission(Subscriber target, Store store) throws NoPermissionException {

        synchronized (target.id < id ? target.permissions : permissions) {
            synchronized (target.id < id ? permissions : target.permissions) {

                // check this user has the permission to perform this action
                validatePermission(AppointerPermission.getInstance(target, store));

                target.removeOwnerPermission(store);

                // remove this user's permission to change the target's permissions
                removePermission(AppointerPermission.getInstance(target, store));
            }
        }
    }

    public void addInventoryManagementPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsNotManagerException {

        addPermissionToManager(target, store, ManageInventoryPermission.getInstance(store));
    }

    public void removeInventoryManagementPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsOwnerException {

        removePermissionFromManager(target, store, ManageInventoryPermission.getInstance(store));
    }

    public void addGetHistoryPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsNotManagerException {

        addPermissionToManager(target, store, GetHistoryPermission.getInstance(store));
    }

    public void removeGetHistoryPermission(Subscriber target, Store store) throws NoPermissionException, TargetIsOwnerException {

        removePermissionFromManager(target, store, GetHistoryPermission.getInstance(store));
    }

    void addPermissionToManager(Subscriber target, Store store, Permission permission) throws NoPermissionException, TargetIsNotManagerException {

        synchronized (target.id < id ? target.permissions : permissions) {
            synchronized (target.id < id ? permissions : target.permissions) {

                // check this user has the permission to perform this action
                validatePermission(AppointerPermission.getInstance(target, store));

                if (!target.havePermission(ManagerPermission.getInstance(store)))
                    throw new TargetIsNotManagerException(target.getUserName(), store.getName());

                // add the permission to the target (if he doesn't already have it)
                target.addPermission(permission);
            }
        }
    }

    void removePermissionFromManager(Subscriber target, Store store, Permission permission) throws NoPermissionException, TargetIsOwnerException {

        synchronized (target.id < id ? target.permissions : permissions) {
            synchronized (target.id < id ? permissions : target.permissions) {

                // check this user has the permission to perform this action
                validatePermission(AppointerPermission.getInstance(target, store));

                if (target.havePermission(OwnerPermission.getInstance(store)))
                    throw new TargetIsOwnerException(target.getUserName(), store.getName());

                target.removePermission(permission);
            }
        }
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

        synchronized (permissions) {

            StringBuilder result = new StringBuilder();

            Permission ownerPermission = OwnerPermission.getInstance(store);
            Permission managerPermission = ManagerPermission.getInstance(store);
            Permission manageInventoryPermission = ManageInventoryPermission.getInstance(store);

            if (havePermission(ownerPermission))
                result.append(ownerPermission.toString()).append(" ");
            if (havePermission(managerPermission))
                result.append(managerPermission.toString()).append(" ");
            if (havePermission(manageInventoryPermission))
                result.append(manageInventoryPermission.toString()).append(" ");

            return result.toString();
        }
    }

    public Collection<String> getEventLog(Collection<String> log) throws NoPermissionException {

        // check this user has the permission to perform this action
        validatePermission(AdminPermission.getInstance());

        return log;
    }

    public Collection<String> getSalesHistoryByStore(Store store) throws NoPermissionException {

        validateAtLeastOnePermission(AdminPermission.getInstance(), GetHistoryPermission.getInstance(store));

        return store.getPurchaseHistory();
    }

    public Collection<String> getPurchaseHistory() {

        return new ArrayList<>(purchaseHistory);
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
