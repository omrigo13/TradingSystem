package permissions;

import store.Store;
import user.User;

public class ManagerPermission extends Permission {

    public ManagerPermission(User user, Store store) {
        super(user, store);
    }

    @Override
    public boolean doCommand(Command command) throws Exception {
        return false;
        // TODO add view command
    }
}
