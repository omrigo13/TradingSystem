package permissions;

import exceptions.ConnectionIdDoesNotExistException;
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
                                                         String targetUserName, Store store)
            throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        return new AddManagerPermissionCommand(tradingSystem.getSubscriberByConnectionId(connectionId),
                tradingSystem.getSubscriberByUserName(targetUserName), store);
    }

    @Override
    public void execute() throws Exception {
        Permission managerPermission = new ManagerPermission(store);
        if (target.havePermission(managerPermission)) {
            target.addPermission(managerPermission);
            user.addPermission(new DeletePermissionPermission(target, store)); // permission to delete the target's permission
        }
    }
}
