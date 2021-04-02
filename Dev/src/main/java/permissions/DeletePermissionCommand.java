package permissions;

import store.Store;
import user.Subscriber;
import user.User;

public class DeletePermissionCommand extends Command {
    private final Subscriber target;
    private final Store store;
    private final Permission permission;

    public DeletePermissionCommand(Subscriber user, Subscriber target, Store store, Permission permission) {
        super(new DeletePermissionPermission(target, store), user);
        this.store = store;
        this.target = target;
        this.permission = permission;
    }

    @Override
    public void execute() throws Exception {
        // TODO deleting owner permission should also delete manager and inventory permissions
        target.deletePermission(permission);
    }
}
