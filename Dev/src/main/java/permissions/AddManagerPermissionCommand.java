package permissions;

import store.Store;
import user.User;

public class AddManagerPermissionCommand extends Command {

    private final User target;
    private final User source;

    public AddManagerPermissionCommand(Store store, User target, User source) {
        super(store);
        this.target = target;
        this.source = source;
    }

    @Override
    public void doCommand() throws Exception {
        Permission permission = new ManagerPermission(target, store);
        if(!target.havePermission(permission) && !target.havePermission(new OwnerPermission(target, store))){
            target.addPermission(new ManagerPermission(target, store));
            source.addPermission(new DeletePermissionPermission(source, target, store));
        }
    }
}
