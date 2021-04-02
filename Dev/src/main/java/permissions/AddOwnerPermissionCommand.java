package permissions;

import exceptions.AlreadyManagerException;
import exceptions.ConnectionIdDoesNotExistException;
import exceptions.NoPermissionException;
import exceptions.SubscriberDoesNotExistException;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;
import user.User;

public class AddOwnerPermissionCommand extends Command {
    private final Subscriber target;
    private final Store store;

    public AddOwnerPermissionCommand(Subscriber user, Subscriber target, Store store) {
        super(new OwnerPermission(store), user);
        this.target = target;
        this.store = store;
    }

    public static Command newAddOwnerPermissionCommand(TradingSystem tradingSystem, String connectionId,
                                                       String targetUserName, int storeId)
            throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        return new AddOwnerPermissionCommand(tradingSystem.getSubscriberByConnectionId(connectionId),
                tradingSystem.getSubscriberByUserName(targetUserName), tradingSystem.getStore(storeId));
    }

    @Override
    public void execute() throws Exception {

        if (!user.havePermission(requiredPermission))
            throw new NoPermissionException();

        Permission managerPermission = new ManagerPermission(store);
        if (target.havePermission(managerPermission))
            throw new AlreadyManagerException();

        // give the target manager and owner permission
        target.addPermission(new OwnerPermission(store));
        target.addPermission(managerPermission);

        // give the user permission to delete the new permission that was added to the target
        user.addPermission(new DeletePermissionPermission(target, store));
    }
}
