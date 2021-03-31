package permissions;

import store.Store;
import user.User;

public class DeletePermissionCommand extends Command {
    private User user;
    private Permission permission;

    public DeletePermissionCommand(Store store, User user, Permission permission) {
        super(store);
        this.user = user;
        this.permission = permission;
    }

    @Override
    public void doCommand() throws Exception {
        user.deletePermission(permission);
    }
}
