package permissions;

import exceptions.ConnectionIdDoesNotExistException;
import exceptions.NoPermissionException;
import exceptions.SubscriberDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateStoreItemCommandTest {

    @Mock TradingSystem tradingSystem;
    @Mock Store store;
    @Mock Subscriber user;

    private final String connectionId = "435263456315236";
    private final int storeId = 234324;
    private final int itemId = 4332423;
    private final String newSubCategory = "Oldies";
    private final int newQuantity = 3;
    private final double newPrice = 700.0;

    @BeforeEach
    void setUp() throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {
        when(tradingSystem.getStore(storeId)).thenReturn(store);
        when(tradingSystem.getSubscriberByConnectionId(connectionId)).thenReturn(user);
    }

    @Test
    void execute() throws Exception {

        Command cmd = UpdateStoreItemCommand.newUpdateStoreItemCommand(tradingSystem, connectionId, storeId, itemId,
                newSubCategory, newQuantity, newPrice);
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(true);
        cmd.execute();
        verify(store).changeQuantity("" + itemId, null, newSubCategory, newQuantity);
    }

    @Test
    void executeNoPermission() throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        Command cmd = UpdateStoreItemCommand.newUpdateStoreItemCommand(tradingSystem, connectionId, storeId, itemId,
                newSubCategory, newQuantity, newPrice);
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(false);
        assertThrows(NoPermissionException.class, cmd::execute);
    }
}