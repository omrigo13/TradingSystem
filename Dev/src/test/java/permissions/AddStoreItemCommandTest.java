package permissions;

import exceptions.NoPermissionException;
import org.junit.jupiter.api.Test;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;

import static org.mockito.Mockito.*;
import static permissions.AddStoreItemCommand.newAddStoreItemCommand;
import static org.junit.jupiter.api.Assertions.*;

class AddStoreItemCommandTest {

    @Test
    void execute() throws Exception {

        TradingSystem tradingSystem = mock(TradingSystem.class);
        Subscriber user = mock(Subscriber.class);
        Store store = mock(Store.class);
        String connectionId = "412342342";
        String storeId = "345345325";
        String item = "X-Box";
        String category = "Electronics";
        String subCategory = "Gaming Consoles";
        int quantity = 1;
        double price = 500.0;

        when(tradingSystem.getStore(storeId)).thenReturn(store);
        when(tradingSystem.getSubscriberByConnectionId(connectionId)).thenReturn(user);
        Command cmd = newAddStoreItemCommand(tradingSystem, connectionId, storeId, item, category, subCategory,
                quantity, price);

        // test performing the command without the required permission
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(false);
        assertThrows(NoPermissionException.class, cmd::execute);

        // test performing the command with the required permission
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(true);
        cmd.execute();
        verify(store).addItem(item, price, category, subCategory, quantity);
    }
}