package user;

import store.Store;

import java.util.Objects;

public class RemovePermissionPermission extends Permission
{
    private final User target;

    public RemovePermissionPermission(User target, Store store) {
        super(store);
        this.target = target;
    }

    public static RemovePermissionPermission getInstance(User target, Store store) {
        int hash = Objects.hash(RemovePermissionPermission.class, target, store);
        RemovePermissionPermission permission = (RemovePermissionPermission)permissions.get(hash);
        if (permission == null) {
            permission = new RemovePermissionPermission(target, store);
            permissions.put(hash, permission);
        }
        return permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RemovePermissionPermission that = (RemovePermissionPermission) o;
        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), target);
    }

    @Override
    public String toString() {
        return "RemovePermissionPermission{" +
                "store=" + store +
                "target=" + target +
                '}';
    }
}
