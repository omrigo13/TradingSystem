package user;

import store.Store;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public abstract class StorePermission extends Permission {

    protected final Store store;

    protected StorePermission(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o || (o != null && getClass() == o.getClass())) return true;
        if (!super.equals(o)) return false;
        StorePermission that = (StorePermission) o;
        return Objects.equals(store, that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), store);
    }

    @Override
    public String toString() {
        return getClass().toString() + "{" +
                "store=" + store +
                '}';
    }
}
