package user;

import store.Store;

import java.lang.ref.WeakReference;

public class GetHistoryPermission extends StorePermission
{
    private GetHistoryPermission(Store store) {
        super(store);
    }

    public static GetHistoryPermission getInstance(Store store) {

        return (GetHistoryPermission)pool.computeIfAbsent(new GetHistoryPermission(store), WeakReference::new).get();
    }

    @Override
    public String toString() {
        return "GetHistoryPermission{" +
                "store=" + (store == null ? null : store.getName()) +
                '}';
    }
}
