package user;

import store.Store;

import java.util.Objects;

public class OwnerPermission extends StorePermission
{
    private OwnerPermission(Store store) {
        super(store);
    }

    public static OwnerPermission getInstance(Store store) {
        int hash = Objects.hash(OwnerPermission.class, store);
        OwnerPermission permission = (OwnerPermission)permissions.get(hash);
        if (permission == null) {
            permission = new OwnerPermission(store);
            permissions.put(hash, permission);
        }
        return permission;
    }

    @Override
    public String toString() {
        return "OwnerPermission{" +
                "store=" + store.getName() +
                '}';
    }
}
