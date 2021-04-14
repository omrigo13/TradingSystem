package user;

import store.Store;

import java.lang.ref.WeakReference;

public class GetHistoryPermission extends StorePermission
{
    private GetHistoryPermission(Store store) {
        super(store);
    }

    public static GetHistoryPermission getInstance(Store store) {

        GetHistoryPermission key = new GetHistoryPermission(store);
        return (GetHistoryPermission)pool.computeIfAbsent(key, k -> new WeakReference<>(key)).get();
    }

    @Override
    public String toString() {
        return "GetHistoryPermission{" +
                "store=" + store.getName() +
                '}';
    }
}
