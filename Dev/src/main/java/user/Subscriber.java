package user;

import exceptions.*;
import store.Item;
import store.Store;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Subscriber extends User {

    private final String userName;
    private final Set<Permission> permissions;

    public Subscriber(String userName, Map<Store, Basket> baskets, Set<Permission> permissions) {
        super(baskets);
        this.userName = userName;
        this.permissions = permissions;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public Subscriber getSubscriber() {
        return this;
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

    public void addManagerPermission(Subscriber target, Store store) throws NoPermissionException, AlreadyOwnerException {

        // check this user has the permission to perform this action
        validatePermission(OwnerPermission.getInstance(store));

        // check if the target is already a manager at this store
        Permission managerPermission = ManagerPermission.getInstance(store);
        if (target.havePermission(managerPermission))
            throw new AlreadyOwnerException(userName);

        // add manager permission to the target
        target.addPermission(managerPermission);

        // give the user permission to delete the new permission that was added to the target
        addPermission(new RemovePermissionPermission(target, store));
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

        // add owner, manager and inventory management permissions to the target
        target.addPermission(ownerPermission);
        target.addPermission(ManagerPermission.getInstance(store));
        target.addPermission(ManageInventoryPermission.getInstance(store));

        // give the user permission to delete the new permission that was added to the target
        addPermission(new RemovePermissionPermission(target, store));
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

        // TODO think if we need this method

        // check this user has the permission to perform this action
        validatePermission(RemovePermissionPermission.getInstance(target, store));

        // perform the action (delete the target's permission)
        target.removePermission(permission);

        // TODO for now all we do is delete the permission (if the target has it). need to think on:
        // remove user's delete-permission permission if the target is no longer store manager?
        // ok to delete some else's manager permission but keep owner?
        // what happens when you give yourself permissions (is that relevant?)
    }

    public void addStoreItem(Store store, String item, String category, String subCategory, int quantity, double price)
            throws NoPermissionException, AddStoreItemException {

        // check this user has the permission to perform this action
        validatePermission(ManageInventoryPermission.getInstance(store));
        try {
            // add the item to the store
            store.addItem(item, price, category, subCategory, quantity);
        } catch (Exception e) {
            throw new AddStoreItemException(store.getName(), item, price, category, subCategory, quantity, e);
        }
    }

    public void removeStoreItem(Store store, int itemId) throws NoPermissionException, RemoveStoreItemException {

        // check this user has the permission to perform this action
        validatePermission(ManageInventoryPermission.getInstance(store));

        // remove the item from the store
        try {
            store.removeItem("" + itemId, null, null);
        } catch (Exception e) {
            throw new RemoveStoreItemException(e);
        }
    }

    public void updateStoreItem(Store store, int itemId, String newSubCategory, Integer newQuantity, Double newPrice)
            throws NoPermissionException, UpdateStoreItemException {

        // check this user has the permission to perform this action
        validatePermission(ManageInventoryPermission.getInstance(store));

        // update the item in the store
        try {
            store.changeQuantity("" + itemId, null, newSubCategory, newQuantity);
            // TODO implement other types of updates
        } catch (Exception e) {
            throw new UpdateStoreItemException(e);
        }
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
}
