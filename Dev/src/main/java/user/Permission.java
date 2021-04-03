package user;

import store.Store;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public abstract class Permission
{
    protected static final Map<Integer, Permission> permissions = Collections.synchronizedMap(new WeakHashMap<>());

    protected final Store store;

    public Permission(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return store.equals(that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), store);
    }

    @Override
    public String toString() {
        return getClass().toString() + "{" +
                "store=" + store +
                '}';
    }
}
