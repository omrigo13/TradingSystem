package permissions;

import exceptions.AlreadyManagerException;
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
import static org.mockito.Mockito.*;
import static permissions.AddOwnerPermissionCommand.newAddOwnerPermissionCommand;

@ExtendWith(MockitoExtension.class)
class AddOwnerPermissionCommandTest {

    @Mock TradingSystem tradingSystem;
    @Mock Subscriber user;
    @Mock Subscriber target;
    @Mock Store store;

    private final String connectionId = "CONNECTION_ID_STRING";
    private final String targetUserName = "NEW OWNER";
    private final int storeId = 2353151;

    @BeforeEach
    void setUp() throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {
        when(tradingSystem.getStore(storeId)).thenReturn(store);
        when(tradingSystem.getSubscriberByConnectionId(connectionId)).thenReturn(user);
        when(tradingSystem.getSubscriberByUserName(targetUserName)).thenReturn(target);
    }

    @Test
    void execute() throws Exception { // TODO remove exception

        Command cmd = newAddOwnerPermissionCommand(tradingSystem, connectionId, targetUserName, storeId);
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(true);
        when(target.havePermission(eq(new ManagerPermission(store)))).thenReturn(false);
        cmd.execute();
        verify(target).addPermission(eq(new OwnerPermission(store)));
        verify(target).addPermission(eq(new ManagerPermission(store)));
        verify(user).addPermission(eq(new DeletePermissionPermission(target, store)));
    }

    @Test
    void executeAlreadyManager() throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        Command cmd = newAddOwnerPermissionCommand(tradingSystem, connectionId, targetUserName, storeId);
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(true);
        when(target.havePermission(eq(new ManagerPermission(store)))).thenReturn(true);
        assertThrows(AlreadyManagerException.class, cmd::execute);
    }

    @Test
    void executeNoPermission() throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {

        Command cmd = newAddOwnerPermissionCommand(tradingSystem, connectionId, targetUserName, storeId);
        when(user.havePermission(cmd.getRequiredPermission())).thenReturn(false);
        assertThrows(NoPermissionException.class, cmd::execute);
    }
}