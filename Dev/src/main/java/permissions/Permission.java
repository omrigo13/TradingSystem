package permissions;

import store.Store;

import java.util.Objects;

public abstract class Permission
{
    private final Store store;

    public Permission(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
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
        return Objects.hash(store);
    }
}
