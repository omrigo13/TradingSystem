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
import static permissions.AddStoreItemCommand.newAddStoreItemCommand;

@ExtendWith(MockitoExtension.class)
class AddStoreItemCommandTest {

    @Mock TradingSystem tradingSystem;
    @Mock Subscriber user;
    @Mock Store store;

    private final String connectionId = "412342342";
    private final int storeId = 345345325;
    private final String item = "X-Box";
    private final String category = "Electronics";
    private final String subCategory = "Gaming Consoles";
    private final int quantity = 1;
    private final double price = 500.0;

    @BeforeEach
    void setUp() throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {
        when(tradingSystem.getStore(storeId)).thenReturn(store);
        when(tradingSystem.getSubscriberByConnectionId(connectionId)).thenReturn(user);
    }

    @Test
    void execute() throws Exception { // TODO exception

        Command cmd = newAddStoreItemCommand(tradingSystem, connectionId, storeId, item, category, subCategory,
                quantity, price);
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(true);
        cmd.execute();
        verify(store).addItem(item, price, category, subCategory, quantity);
    }

    @Test
    void executeNoPermission() throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        Command cmd = newAddStoreItemCommand(tradingSystem, connectionId, storeId, item, category, subCategory,
                quantity, price);
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(false);
        assertThrows(NoPermissionException.class, cmd::execute);
    }
}