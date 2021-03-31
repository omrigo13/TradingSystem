package permissions;

import store.Store;
import user.User;

public class AddItemToStorePermission extends Permission {

    public AddItemToStorePermission(User user, Store store) {
        super(user, store);
    }

    @Override
    public boolean doCommand(Command command) throws Exception {
            if (command instanceof AddItemToStoreCommand && command.getStore() == store)
            {
                command.doCommand();
                return true;
            }
            return false;
        }
    }
