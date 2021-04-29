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

        return (ManageInventoryPermission)pool.computeIfAbsent(new ManageInventoryPermission(store), WeakReference::new).get();
    }

    @Override
    public String toString() {
        return "ManageInventoryPermission{" +
                "store=" + (store == null ? null : store.getName()) +
                '}';
    }
}
