package user;

import store.Store;

import javax.persistence.Entity;
import java.lang.ref.WeakReference;
import java.util.Objects;

@Entity
public class ManageInventoryPermission extends StorePermission
{
    private ManageInventoryPermission(Store store) {
        super(store);
    }

    public ManageInventoryPermission() {
    }

    public static ManageInventoryPermission getInstance(Store store) {
        return getInstance(new ManageInventoryPermission(store));
    }

    @Override
    public String toString() {
        return "ManageInventoryPermission{" +
                "store=" + (store == null ? null : store.getName()) +
                '}';
    }
}
