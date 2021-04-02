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
import static permissions.DeletePermissionCommand.newDeletePermissionCommand;

@ExtendWith(MockitoExtension.class)
class DeletePermissionCommandTest {

    @Mock TradingSystem tradingSystem;
    @Mock Subscriber user;
    @Mock Subscriber target;
    @Mock Store store;

    private final Permission permission = new ManagerPermission(store);
    private final String connectionId = "CONNECTION_ID_STRING";
    private final String targetUserName = "NEW MANAGER";
    private final int storeId = 8984356;

    @BeforeEach
    void setUp() throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {
        when(tradingSystem.getStore(storeId)).thenReturn(store);
        when(tradingSystem.getSubscriberByConnectionId(connectionId)).thenReturn(user);
        when(tradingSystem.getSubscriberByUserName(targetUserName)).thenReturn(target);
    }

    @Test
    void execute() throws Exception { // TODO remove exception

        Command cmd = newDeletePermissionCommand(tradingSystem, connectionId, targetUserName, storeId, permission);
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(true);
        cmd.execute();
        verify(target).deletePermission(permission);
    }

    @Test
    void executeNoPermission() throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        Command cmd = newDeletePermissionCommand(tradingSystem, connectionId, targetUserName, storeId, permission);
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(false);
        assertThrows(NoPermissionException.class, cmd::execute);
    }
}