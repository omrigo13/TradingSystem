package permissions;

import store.Store;
import user.User;

public class OwnerPermission extends Permission {

    public OwnerPermission(User user, Store store) {
        super(user, store);
    }

    @Override
    public boolean doCommand(Command command) throws Exception {
        if(command.getStore() == store && !(command instanceof DeletePermissionCommand))
            command.doCommand();
        return true;
    }
}
