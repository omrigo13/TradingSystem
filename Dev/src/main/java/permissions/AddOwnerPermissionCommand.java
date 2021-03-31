package permissions;

import store.Store;
import user.User;

public class AddOwnerPermissionCommand extends Command {
    private final User target;
    private final User source;

    public AddOwnerPermissionCommand(Store store, User target, User source) {
        super(store);
        this.target = target;
        this.source = source;
    }

    @Override
    public void doCommand() throws Exception {
        Permission permission = new OwnerPermission(target, store);
        if(!target.havePermission(permission) && !target.havePermission(new ManagerPermission(target, store))){
            target.addPermission(new OwnerPermission(target, store));
            source.addPermission(new DeletePermissionPermission(source, target, store));
        }
    }
}
