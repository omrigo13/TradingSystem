package permissions;

import store.Store;
import user.User;

public class DeletePermissionPermission extends Permission {
    final User target;

    public DeletePermissionPermission(User source, User target, Store store) {
        super(source, store);
        this.target = target;
    }

    @Override
    public boolean doCommand(Command command) throws Exception {
        if (command instanceof DeletePermissionCommand && command.getStore() == store)
        {
            command.doCommand();
            return true;
        }
        return false;
    }
}
