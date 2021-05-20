package user;

import javax.persistence.*;
import javax.persistence.MappedSuperclass;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
@Entity
//@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Permission {
    @Id
    @GeneratedValue
    private int id;

    protected static final Map<Permission, WeakReference<Permission>> pool = Collections.synchronizedMap(new WeakHashMap<>());

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Map<Permission, WeakReference<Permission>> getPool() {
        return pool;
    }
}
