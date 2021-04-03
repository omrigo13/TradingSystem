package user;

import store.Store;

import java.util.Objects;

public class ManageInventoryPermission extends Permission
{
    public ManageInventoryPermission(Store store) {
        super(store);
    }

    public static ManageInventoryPermission getInstance(Store store) {
        int hash = Objects.hash(ManageInventoryPermission.class, store);
        ManageInventoryPermission permission = (ManageInventoryPermission)permissions.get(hash);
        if (permission == null) {
            permission = new ManageInventoryPermission(store);
            permissions.put(hash, permission);
        }
        return permission;
    }
}
