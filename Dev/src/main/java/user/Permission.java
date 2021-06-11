package user;

import org.hibernate.cfg.InheritanceState;
import persistence.Repo;

import javax.persistence.*;
import java.lang.ref.WeakReference;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Permission {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToMany
    protected static Map<Permission, Permission> pool = Collections.synchronizedMap(new HashMap<>());

    protected static <T extends Permission> T getInstance(T permission) {
        pool.computeIfAbsent(permission, k -> {
            Repo.persist(permission);
            return permission;
        });
        return (T)pool.get(permission);
    }

    public static Map<Permission, Permission> getPool() {
        return pool;
    }

    public static void setPool(Map<Permission, Permission> pool) {
        Permission.pool = pool;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
