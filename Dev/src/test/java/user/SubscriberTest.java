package user;

import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Store;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriberTest {

    @InjectMocks private Subscriber subscriber;

    @Mock private Permission permission;
    @Mock private Set<Permission> permissions;
    @Mock private Collection<Store> stores;
    @Mock private ItemException itemException;

    private final Store store = mock(Store.class);
    private final Subscriber target = mock(Subscriber.class);
    private final Permission adminPermission = AdminPermission.getInstance();
    private final Permission managerPermission = ManagerPermission.getInstance(store);
    private final Permission ownerPermission = OwnerPermission.getInstance(store);
    private final Permission manageInventoryPermission = ManageInventoryPermission.getInstance(store);
    private final Permission removePermissionPermission = RemovePermissionPermission.getInstance(target, store);

    private final double price = 500.0;
    private final int quantity = 3;
    private final int itemId = 37373;
    private final String item = "PS5";
    private final String category = "Electronics";
    private final String subCategory = "Gaming Consoles";

    @Test
    void validatePermission_HavePermission() throws NoPermissionException {
        when(permissions.contains(permission)).thenReturn(true);
        subscriber.validatePermission(permission);
    }

    @Test
    void validatePermission_NoPermission() {
        assertThrows(NoPermissionException.class, () -> subscriber.validatePermission(permission));
    }

    @Test
    void validateAtLeastOnePermission_HavePermission() throws NoPermissionException {
        when(permissions.contains(permission)).thenReturn(false);
        when(permissions.contains(adminPermission)).thenReturn(true);
        subscriber.validateAtLeastOnePermission(permission, adminPermission);
    }

    @Test
    void validateAtLeastOnePermission_NoPermission() {
        assertThrows(NoPermissionException.class, () -> subscriber.validateAtLeastOnePermission(adminPermission, permission));
    }

    @Test
    void getAllStores() throws NoPermissionException {
        when(permissions.contains(adminPermission)).thenReturn(true);
        assertEquals(stores, subscriber.getAllStores(stores));
    }

    @Test
    void getAllStores_NoPermission() {
        assertThrows(NoPermissionException.class, () -> subscriber.getAllStores(stores));
    }

    @Test
    void addManagerPermission() throws NoPermissionException, AlreadyManagerException {

        when(permissions.contains(ownerPermission)).thenReturn(true);
        subscriber.addManagerPermission(target, store);
        verify(target).addPermission(managerPermission);
        verify(permissions).add(removePermissionPermission);
    }

    @Test
    void addManagerPermission_NoPermission() {

        assertThrows(NoPermissionException.class, () -> subscriber.addManagerPermission(target, store));
        verifyNoInteractions(target);
    }

    @Test
    void addManagerPermission_AlreadyManager() {

        when(permissions.contains(ownerPermission)).thenReturn(true);
        when(target.havePermission(managerPermission)).thenReturn(true);
        assertThrows(AlreadyManagerException.class, () -> subscriber.addManagerPermission(target, store));
        verify(target, never()).addPermission(any());
        verify(permissions, never()).add(any());
    }

    @Test
    void addOwnerPermission() throws NoPermissionException, AlreadyOwnerException {

        when(permissions.contains(ownerPermission)).thenReturn(true);
        subscriber.addOwnerPermission(target, store);
        verify(target).addPermission(ownerPermission);
        verify(target).addPermission(managerPermission);
        verify(target).addPermission(manageInventoryPermission);
        verify(permissions).add(removePermissionPermission);
    }

    @Test
    void addOwnerPermission_NoPermission() {

        assertThrows(NoPermissionException.class, () -> subscriber.addOwnerPermission(target, store));
        verifyNoInteractions(target);
    }

    @Test
    void addOwnerPermission_AlreadyOwner() {

        when(permissions.contains(ownerPermission)).thenReturn(true);
        when(target.havePermission(ownerPermission)).thenReturn(true);
        assertThrows(AlreadyOwnerException.class, () -> subscriber.addOwnerPermission(target, store));
        verify(target, never()).addPermission(any());
        verify(permissions, never()).add(any());
    }

    @Test
    void addOwnerPermission_ManagerAppointedByCaller() throws AlreadyOwnerException, NoPermissionException {

        when(permissions.contains(ownerPermission)).thenReturn(true);
        when(target.havePermission(managerPermission)).thenReturn(true);
        when(permissions.contains(removePermissionPermission)).thenReturn(true);
        subscriber.addOwnerPermission(target, store);
        verify(target).addPermission(ownerPermission);
        verify(target).addPermission(managerPermission);
        verify(target).addPermission(manageInventoryPermission);
        verify(permissions).add(removePermissionPermission);    }

    @Test
    void addOwnerPermission_ManagerAppointedByAnother() {

        when(permissions.contains(ownerPermission)).thenReturn(true);
        when(target.havePermission(managerPermission)).thenReturn(true);
        when(permissions.contains(removePermissionPermission)).thenReturn(false);
        assertThrows(NoPermissionException.class, () -> subscriber.addOwnerPermission(target, store));
        verify(target, never()).addPermission(any());
        verify(permissions, never()).add(any());
    }

    @Test
    void removeOwnerPermission() throws NoPermissionException {

        when(permissions.contains(removePermissionPermission)).thenReturn(true);
        subscriber.removeOwnerPermission(target, store);
        verify(target).removePermission(ownerPermission);
        verify(target).removePermission(manageInventoryPermission);
        verify(target).removePermission(managerPermission);
    }

    @Test
    void removeManagerPermission() throws NoPermissionException {

        when(permissions.contains(removePermissionPermission)).thenReturn(true);
        subscriber.removeManagerPermission(target, store);
        verify(target).removePermission(manageInventoryPermission);
        verify(target).removePermission(managerPermission);
    }

    @Test
    void removePermission_NoPermission() {

        assertThrows(NoPermissionException.class, () -> subscriber.removePermission(target, store, permission));
    }

    @Test
    void addStoreItem() throws ItemException, NoPermissionException {

        when(permissions.contains(manageInventoryPermission)).thenReturn(true);
        subscriber.addStoreItem(itemId,store, item, category, subCategory, quantity, price);
        verify(store).addItem(itemId,item, price, category, subCategory, quantity);
    }

    @Test
    void addStoreItem_NoPermission() throws ItemException {

        assertThrows(NoPermissionException.class, () -> subscriber.addStoreItem(itemId,store, item, category, subCategory, quantity, price));
        verify(store, never()).addItem(anyInt(), any(), anyDouble(), any(), any(), anyInt());
    }

    @Test
    void addStoreItem_AddItemException() throws ItemException {

        when(permissions.contains(manageInventoryPermission)).thenReturn(true);
        doThrow(itemException).when(store).addItem(itemId,item, price, category, subCategory, quantity);
        assertThrows(ItemException.class, () -> subscriber.addStoreItem(itemId,store, item, category, subCategory, quantity, price));
    }

    @Test
    void removeStoreItem() throws ItemException, NoPermissionException {

        when(permissions.contains(manageInventoryPermission)).thenReturn(true);
        subscriber.removeStoreItem(store, itemId);
        verify(store).removeItem(itemId);
    }

    @Test
    void removeStoreItem_NoPermission() throws ItemException {

        assertThrows(NoPermissionException.class, () -> subscriber.removeStoreItem(store, itemId));
        verify(store, never()).removeItem(anyInt());
    }

    @Test
    void removeStoreItem_RemoveItemException() throws ItemException {

        when(permissions.contains(manageInventoryPermission)).thenReturn(true);
        doThrow(itemException).when(store).removeItem(itemId);
        assertThrows(ItemException.class, () -> subscriber.removeStoreItem(store, itemId));
    }

    @Test
    void updateStoreItem() throws ItemException, NoPermissionException {

        when(permissions.contains(manageInventoryPermission)).thenReturn(true);
        subscriber.updateStoreItem(store, itemId, subCategory, quantity, price);
        verify(store).changeItem(itemId, subCategory, quantity, price);
    }

    @Test
    void updateStoreItem_NoPermission() throws ItemException {

        assertThrows(NoPermissionException.class,
                () -> subscriber.updateStoreItem(store, itemId, subCategory, quantity, price));
        verify(store, never()).changeItem(anyInt(), any(), anyInt(), any());
    }

    @Test
    void updateStoreItem_ChangeQuantityException() throws ItemException {

        when(permissions.contains(manageInventoryPermission)).thenReturn(true);
        doThrow(itemException).when(store).changeItem(itemId, subCategory, quantity, price);
        assertThrows(ItemException.class, () -> subscriber.updateStoreItem(store, itemId, subCategory, quantity, price));
    }
}