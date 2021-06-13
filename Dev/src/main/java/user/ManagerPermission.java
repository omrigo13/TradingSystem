package user;

import store.Store;

import javax.persistence.Entity;
import java.lang.ref.WeakReference;
import java.util.Objects;

@Entity
public class ManagerPermission extends StorePermission
{
    private ManagerPermission(Store store) {
        super(store);
    }

    public ManagerPermission() {
    }

    public static ManagerPermission getInstance(Store store) {
        return getInstance(new ManagerPermission(store));
    }

    @Override
    public String toString() {
        return "ManagerPermission{" +
                "store=" + (store == null ? null : store.getName()) +
                '}';
    }
}
