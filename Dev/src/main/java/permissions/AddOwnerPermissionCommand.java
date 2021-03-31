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
        target.addPermission(new OwnerPermission(target, getStore()));
        source.addPermission(new DeletePermissionPermission(source, target, getStore()));
    }
}
