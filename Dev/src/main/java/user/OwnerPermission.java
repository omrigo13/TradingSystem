package user;

import store.Store;

import javax.persistence.Entity;
import java.lang.ref.WeakReference;
import java.util.Objects;

@Entity
public class OwnerPermission extends StorePermission
{
    private OwnerPermission(Store store) {
        super(store);
    }

    public OwnerPermission() {
    }

    public static OwnerPermission getInstance(Store store) {
        return getInstance(new OwnerPermission(store));
    }

    @Override
    public String toString() {
        return "OwnerPermission{" +
                "store=" + (store == null ? null : store.getName()) +
                '}';
    }
}
