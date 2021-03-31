package permissions;

import store.Store;
import user.User;

public class ManageInventoryPermission extends Permission {

    public ManageInventoryPermission(User user, Store store) {
        super(user, store);
    }

    @Override
    public boolean doCommand(Command command) throws Exception {
        if (command.getStore() == store) {
            if (command instanceof AddItemCommand || command instanceof DeleteItemCommand || command instanceof ChangeItemCommand) {
                command.doCommand();
                return true;
            }
        }
        return false;
    }
}
