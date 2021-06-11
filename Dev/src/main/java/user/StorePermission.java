package user;

import store.Store;

import javax.persistence.*;
import java.util.Objects;

@MappedSuperclass
public abstract class StorePermission extends Permission {

    @ManyToOne
    protected Store store;

    public StorePermission() {
    }

    protected StorePermission(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || !super.equals(o)) return false;
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
                "store=" + (store == null ? null : store.getName()) +
                '}';
    }
}
