package user;

import store.Store;

import javax.persistence.Entity;
import java.lang.ref.WeakReference;

@Entity
public class GetHistoryPermission extends StorePermission
{
    private GetHistoryPermission(Store store) {
        super(store);
    }

    public GetHistoryPermission() {
    }

    public static GetHistoryPermission getInstance(Store store) {
        return getInstance(new GetHistoryPermission(store));
    }

    @Override
    public String toString() {
        return "GetHistoryPermission{" +
                "store=" + (store == null ? null : store.getName()) +
                '}';
    }
}
