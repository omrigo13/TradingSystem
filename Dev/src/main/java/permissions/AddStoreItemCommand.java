package permissions;

import exceptions.ConnectionIdDoesNotExistException;
import exceptions.NoPermissionException;
import exceptions.SubscriberDoesNotExistException;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;
import user.User;

public final class AddStoreItemCommand extends Command {

    private final Store store;
    private final String productName;
    private final String category;
    private final String subCategory;
    private final int quantity;
    private final double price;

    private AddStoreItemCommand(Subscriber user, Store store, String productName, String category, String subCategory,
                                int quantity, double price) {
        super(new ManageInventoryPermission(store), user);
        this.store = store;
        this.productName = productName;
        this.category = category;
        this.subCategory = subCategory;
        this.quantity = quantity;
        this.price = price;
    }

    public static Command newAddStoreItemCommand(TradingSystem tradingSystem, String connectionId, int storeId,
                                                 String productName, String category, String subCategory, int quantity,
                                                 double price) throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        return new AddStoreItemCommand(tradingSystem.getSubscriberByConnectionId(connectionId), tradingSystem.getStore(storeId),
                productName, category, subCategory, quantity, price);
    }

    @Override
    public void execute() throws Exception {
        if (!user.havePermission(requiredPermission))
            throw new NoPermissionException();
        store.addItem(productName, price, category, subCategory, quantity);
    }
}
