package user;

import store.Store;

import javax.persistence.Entity;
import java.lang.ref.WeakReference;

@Entity
public class EditPolicyPermission extends StorePermission
{
    private EditPolicyPermission(Store store) {
        super(store);
    }

    public EditPolicyPermission() {
    }

    public static EditPolicyPermission getInstance(Store store) {
        return getInstance(new EditPolicyPermission(store));
    }

    @Override
    public String toString() {
        return "EditPolicyPermission{" +
                "store=" + (store == null ? null : store.getName()) +
                '}';
    }
}
