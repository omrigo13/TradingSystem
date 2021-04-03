package user;

import store.Store;

import java.util.Objects;

public class ManagerPermission extends StorePermission
{
    public ManagerPermission(Store store) {
        super(store);
    }

    public static ManagerPermission getInstance(Store store) {
        int hash = Objects.hash(ManagerPermission.class, store);
        ManagerPermission permission = (ManagerPermission)permissions.get(hash);
        if (permission == null) {
            permission = new ManagerPermission(store);
            permissions.put(hash, permission);
        }
        return permission;
    }

    @Override
    public String toString() {
        return "ManagerPermission{" +
                "store=" + store.getName() +
                '}';
    }
}
