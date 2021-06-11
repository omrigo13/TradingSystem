package user;

import persistence.Repo;
import store.Store;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.lang.ref.WeakReference;
import java.util.Objects;

@Entity
public class AppointerPermission extends StorePermission
{
    @ManyToOne
    private Subscriber target;

    private AppointerPermission(Subscriber target, Store store) {
        super(store);
        this.target = target;
    }

    public AppointerPermission() {
    }

    public Subscriber getTarget() {
        return target;
    }

    public void setTarget(Subscriber target) {
        this.target = target;
    }

    public static AppointerPermission getInstance(Subscriber target, Store store) {
        return getInstance(new AppointerPermission(target, store));
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
                " target=" + (target == null ? null : target.getUserName()) +
                '}';
    }
}
