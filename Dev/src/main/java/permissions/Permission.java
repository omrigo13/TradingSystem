package permissions;

import store.Store;
import user.User;

import java.util.Objects;

public abstract class Permission {

    protected final User user;
    protected final Store store;

    public Permission(User user, Store store) {
        this.user = user;
        this.store = store;
    }

    public abstract boolean doCommand(Command command) throws Exception;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return user.equals(that.user) &&
                store.equals(that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, store);
    }
}
