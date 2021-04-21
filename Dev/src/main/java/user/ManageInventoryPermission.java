package user;

import store.Store;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class ManageInventoryPermission extends StorePermission
{
    private ManageInventoryPermission(Store store) {
        super(store);
    }

    public static ManageInventoryPermission getInstance(Store store) {

        ManageInventoryPermission key = new ManageInventoryPermission(store);
        return (ManageInventoryPermission)pool.computeIfAbsent(key, WeakReference::new).get();
    }

    @Override
    public String toString() {
        return "ManageInventoryPermission{" +
                "store=" + (store == null ? null : store.getName()) +
                '}';
    }
}
