package user;

import store.Store;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class OwnerPermission extends StorePermission
{
    private OwnerPermission(Store store) {
        super(store);
    }

    public static OwnerPermission getInstance(Store store) {

        OwnerPermission key = new OwnerPermission(store);
        return (OwnerPermission)pool.computeIfAbsent(key, k -> new WeakReference<>(key)).get();
    }

    @Override
    public String toString() {
        return "OwnerPermission{" +
                "store=" + store.getName() +
                '}';
    }
}
