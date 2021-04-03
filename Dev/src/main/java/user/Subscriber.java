package user;

import exceptions.*;
import store.Store;

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

    @Override
    public Subscriber getSubscriber() {
        return this;
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public void deletePermission(Permission permission) {
        permissions.remove(permission);
    }

    public boolean havePermission(Permission permission) {
        return permissions.contains(permission);
    }

    public void validatePermission(Permission permission) throws NoPermissionException {
        if (!havePermission(permission))
            throw new NoPermissionException(permission.toString());
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
        addPermission(new RemovePermissionPermission(target, store));
    }

    public void addOwnerPermission(Subscriber target, Store store) throws NoPermissionException, AlreadyManagerException {

        // check this user has the permission to perform this action
        Permission ownerPermission = OwnerPermission.getInstance(store);
        validatePermission(ownerPermission);

        // check if the target is already a manager at this store (an owner is always also a manager)
        Permission managerPermission = ManagerPermission.getInstance(store);
        if (target.havePermission(managerPermission))
            throw new AlreadyManagerException(userName);

        // add owner and manager permissions to the target
        target.addPermission(ownerPermission);
        target.addPermission(managerPermission);

        // give the user permission to delete the new permission that was added to the target
        addPermission(new RemovePermissionPermission(target, store));
    }

    public void removePermission(Subscriber target, Store store, Permission permission) throws NoPermissionException {

        // check this user has the permission to perform this action
        validatePermission(RemovePermissionPermission.getInstance(target, store));

        // perform the action (delete the target's permission)
        target.deletePermission(permission);

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
            throw new AddStoreItemException(e);
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
}
