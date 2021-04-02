package permissions;

import exceptions.AlreadyManagerException;
import exceptions.ConnectionIdDoesNotExistException;
import exceptions.NoPermissionException;
import exceptions.SubscriberDoesNotExistException;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;

public final class AddManagerPermissionCommand extends Command {

    private final Subscriber target;
    private final Store store;

    private AddManagerPermissionCommand(Subscriber user, Subscriber target, Store store) {
        super(new DeletePermissionPermission(target, store), user);
        this.store = store;
        this.target = target;
    }

    public static Command newAddManagerPermissionCommand(TradingSystem tradingSystem, String connectionId,
                                                         String targetUserName, int storeId)
            throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        return new AddManagerPermissionCommand(tradingSystem.getSubscriberByConnectionId(connectionId),
                tradingSystem.getSubscriberByUserName(targetUserName), tradingSystem.getStore(storeId));
    }

    @Override
    public void execute() throws NoPermissionException, AlreadyManagerException {
        Permission managerPermission = new ManagerPermission(store);
        if (!user.havePermission(requiredPermission))
            throw new NoPermissionException();
        if (target.havePermission(managerPermission))
            throw new AlreadyManagerException();

        target.addPermission(managerPermission);
        user.addPermission(new DeletePermissionPermission(target, store)); // permission to delete the target's permission
    }
}
