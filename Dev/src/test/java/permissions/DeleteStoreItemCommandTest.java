package permissions;

import exceptions.NoPermissionException;
import org.junit.jupiter.api.Test;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static permissions.DeleteStoreItemCommand.newDeleteStoreItemCommand;

class DeleteStoreItemCommandTest {

    @Test
    void execute() throws Exception {

        TradingSystem tradingSystem = mock(TradingSystem.class);
        Subscriber user = mock(Subscriber.class);
        Store store = mock(Store.class);
        String connectionId = "2345523532453245";
        String storeId = "345345325";
        int itemId = 4332423;

        when(tradingSystem.getStore(storeId)).thenReturn(store);
        when(tradingSystem.getSubscriberByConnectionId(connectionId)).thenReturn(user);
        Command cmd = newDeleteStoreItemCommand(tradingSystem, connectionId, storeId, itemId);

        // test performing the command without the required permission
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(false);
        assertThrows(NoPermissionException.class, cmd::execute);

        // test performing the command with the required permission
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(true);
        cmd.execute();
        verify(store).removeItem("" + itemId, null, null);
    }
}