package permissions;

import store.Store;
import user.Subscriber;
import user.User;

public class AddOwnerPermissionCommand extends Command {
    private final Subscriber target;
    private final Store store;

    public AddOwnerPermissionCommand(Subscriber user, Subscriber target, Store store) {
        super(new DeletePermissionPermission(target, store), user);
        this.target = target;
        this.store = store;
    }

    @Override
    public void execute() throws Exception {
        Permission managerPermission = new ManagerPermission(store);
        Permission ownerPermission = new OwnerPermission(store);
        if (target.havePermission(managerPermission)) {
            target.addPermission(ownerPermission);
            target.addPermission(managerPermission);
            user.addPermission(new DeletePermissionPermission(target, store)); // permission to delete the target's permission
        }
    }
}
