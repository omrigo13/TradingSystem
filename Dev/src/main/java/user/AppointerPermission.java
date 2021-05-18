package user;

import store.Store;

import java.lang.ref.WeakReference;
import java.util.Objects;
import javax.persistence.*;
@Entity
public class AppointerPermission extends StorePermission
{
    @ManyToOne
    private Subscriber target = null;

    private AppointerPermission(Subscriber target, Store store) {
        super(store);
        this.target = target;
    }

    public AppointerPermission() {

    }

    public Subscriber getTarget() {
        return target;
    }

    public static AppointerPermission getInstance(Subscriber target, Store store) {

        return (AppointerPermission)pool.computeIfAbsent(new AppointerPermission(target, store), WeakReference::new).get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() || !super.equals(o)) return false;
        AppointerPermission that = (AppointerPermission) o;
        return Objects.equals(target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), target);
    }

    @Override
    public String toString() {
        return "AppointerPermission{" +
                "store=" + (store == null ? null : store.getName()) +
                " target=" + (target == null ? null : target.getUsername()) +
                '}';
    }
}
