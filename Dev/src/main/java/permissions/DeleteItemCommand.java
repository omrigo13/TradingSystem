package permissions;

import store.Store;

public class DeleteItemCommand extends Command {

    private final int productID;

    public DeleteItemCommand(Store store, int productID)
    {
        super(store);
        this.productID = productID;
    }

    @Override
    public void doCommand() throws Exception {
        store.removeItem(productID);
    }
}
