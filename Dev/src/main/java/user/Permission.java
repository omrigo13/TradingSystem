package user;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

@MappedSuperclass
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
}
