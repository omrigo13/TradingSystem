package permissions;

import exceptions.NoPermissionException;
import org.junit.jupiter.api.Test;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;
import user.User;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateStoreItemCommandTest {

    @Test
    void execute() throws Exception {
        TradingSystem tradingSystem = mock(TradingSystem.class);
        Store store = mock(Store.class);
        Subscriber user = mock(Subscriber.class);
        String connectionId = "435263456315236";
        String storeId = "345345325";
        int itemId = 4332423;
        String newSubCategory = "Oldies";
        int newQuantity = 3;
        double newPrice = 700.0;

        when(tradingSystem.getStore(storeId)).thenReturn(store);
        when(tradingSystem.getSubscriberByConnectionId(connectionId)).thenReturn(user);
        Command cmd = UpdateStoreItemCommand.newUpdateStoreItemCommand(tradingSystem, connectionId, storeId, itemId,
                newSubCategory, newQuantity, newPrice);

        // test performing the command without the required permission
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(false);
        assertThrows(NoPermissionException.class, cmd::execute);

        // test performing the command with the required permission
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(true);
        cmd.execute();
        verify(store).changeQuantity("" + itemId, null, newSubCategory, newQuantity);
    }
}