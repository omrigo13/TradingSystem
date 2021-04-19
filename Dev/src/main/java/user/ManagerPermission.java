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

        ManagerPermission key = new ManagerPermission(store);
        return (ManagerPermission)pool.computeIfAbsent(key, k -> new WeakReference<>(key)).get();
    }

    @Override
    public String toString() {
        return "ManagerPermission{" +
                "store=" + store.getName() +
                '}';
    }
}
