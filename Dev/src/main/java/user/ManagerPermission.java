package user;

import store.Store;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class ManagerPermission extends StorePermission
{
    private ManagerPermission(Store store) {
        super(store);
    }

    public static ManagerPermission getInstance(Store store) {

        return (ManagerPermission)pool.computeIfAbsent(new ManagerPermission(store), WeakReference::new).get();
    }

    @Override
    public String toString() {
        return "ManagerPermission{" +
                "store=" + (store == null ? null : store.getName()) +
                '}';
    }
}
