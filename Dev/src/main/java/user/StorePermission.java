package user;

import store.Store;

import javax.persistence.*;
import javax.persistence.OneToOne;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
@Entity
public abstract class StorePermission extends Permission {

    @ManyToOne
    protected Store store = null;

    protected StorePermission(Store store) {
        this.store = store;
    }

    public StorePermission() {

    }

    public Store getStore() {
        return store;
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
