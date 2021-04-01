package permissions;

import exceptions.ConnectionIdDoesNotExistException;
import exceptions.NoPermissionException;
import exceptions.SubscriberDoesNotExistException;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;
import user.User;

public class DeleteStoreItemCommand extends Command {
    private final Store store;
    private final int productID;

    private DeleteStoreItemCommand(Subscriber user, Store store, int productID) {
        super(new ManageInventoryPermission(store), user);
        this.store = store;
        this.productID = productID;
    }

    public static Command newDeleteStoreItemCommand(TradingSystem tradingSystem, String connectionId, String storeId, int productId)
            throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        return new DeleteStoreItemCommand(tradingSystem.getSubscriberByConnectionId(connectionId), tradingSystem.getStore(storeId), productId);
    }

    @Override
    public void execute() throws Exception {
        if (!user.havePermission(requiredPermission))
            throw new NoPermissionException();
        store.removeItem("" + productID, null, null);
    }
}
