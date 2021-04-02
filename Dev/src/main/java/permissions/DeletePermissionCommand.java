package permissions;

import exceptions.AlreadyManagerException;
import exceptions.ConnectionIdDoesNotExistException;
import exceptions.NoPermissionException;
import exceptions.SubscriberDoesNotExistException;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;
import user.User;

public class DeletePermissionCommand extends Command {
    private final Subscriber target;
    private final Store store;
    private final Permission permission;

    private DeletePermissionCommand(Subscriber user, Subscriber target, Store store, Permission permission) {
        super(new DeletePermissionPermission(target, store), user);
        this.store = store;
        this.target = target;
        this.permission = permission;
    }

    public static Command newDeletePermissionCommand(TradingSystem tradingSystem, String connectionId,
                                                     String targetUserName, int storeId, Permission permission)
            throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        return new DeletePermissionCommand(tradingSystem.getSubscriberByConnectionId(connectionId),
                tradingSystem.getSubscriberByUserName(targetUserName), tradingSystem.getStore(storeId), permission);
    }

    @Override
    public void execute() throws NoPermissionException {

        if (!user.havePermission(requiredPermission))
            throw new NoPermissionException();

        target.deletePermission(permission);

        // TODO for now all we do is delete the permission (if the target has it). need to think on:
        // remove user's delete-permission permission if the target is no longer store manager?
        // ok to delete some else's manager permission but keep owner?
        // what happens when you give yourself permissions (is that relevant?)
    }
}
