package permissions;

import exceptions.ConnectionIdDoesNotExistException;
import exceptions.NoPermissionException;
import exceptions.SubscriberDoesNotExistException;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;
import user.User;

public final class UpdateStoreItemCommand extends Command {

    private final Store store;
    private final int productId;
    private final String newSubCategory;
    private final Integer newQuantity;
    private final Double newPrice;

    private UpdateStoreItemCommand(Subscriber user, Store store, int productId, String newSubCategory, Integer newQuantity,
                                   Double newPrice) {
        super(new ManageInventoryPermission(store), user);
        this.store = store;
        this.productId = productId;
        this.newSubCategory = newSubCategory;
        this.newQuantity = newQuantity;
        this.newPrice = newPrice;
    }

    public static Command newUpdateStoreItemCommand(TradingSystem tradingSystem, String connectionId, String storeId,
                                                    int productId, String newSubCategory, Integer newQuantity,
                                                    Double newPrice) throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {
        return new UpdateStoreItemCommand(tradingSystem.getSubscriberByConnectionId(connectionId), tradingSystem.getStore(storeId), productId,
                newSubCategory, newQuantity, newPrice);
    }

    @Override
    public void execute() throws Exception {
        if (!user.havePermission(requiredPermission))
            throw new NoPermissionException();
        store.changeQuantity("" + productId, null, newSubCategory, newQuantity); // TODO
    }
}
