package permissions;

import store.Store;
import user.User;

public abstract class Permission {

    protected final User user;
    protected final Store store;

    public Permission(User user, Store store) {
        this.user = user;
        this.store = store;
    }

    public abstract boolean doCommand(Command command) throws Exception;
}
