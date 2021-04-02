package permissions;

import store.Store;
import user.User;

import java.util.Objects;

public class DeletePermissionPermission extends Permission
{
    private final User target;

    public DeletePermissionPermission(User target, Store store) {
        super(store);
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeletePermissionPermission that = (DeletePermissionPermission) o;
        return target.equals(that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), target);
    }
}
