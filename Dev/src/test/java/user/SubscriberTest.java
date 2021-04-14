package user;

import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Item;
import store.Store;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriberTest {

    private Subscriber subscriber;

    @Mock private Permission permission;
    @Mock private Set<Permission> permissions;
    @Mock private Set<Permission> targetPermissions;
    @Mock private Collection<Store> stores;
    @Mock private Store store;
    @Mock private Subscriber target;
    @Mock private Map<Store, Collection<Item>> itemsPurchased;
    @Mock private Collection<String> purchasesDetails;

    private final Permission adminPermission = AdminPermission.getInstance();
    private final Permission managerPermission = ManagerPermission.getInstance(store);
    private final Permission ownerPermission = OwnerPermission.getInstance(store);
    private final Permission manageInventoryPermission = ManageInventoryPermission.getInstance(store);
    private final Permission removePermissionPermission = RemovePermissionPermission.getInstance(target, store);

    private final double price = 500.0;
    private final int quantity = 3;
    private final int itemId = 37373;
    private final String subCategory = "Gaming Consoles";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        subscriber = spy(new Subscriber(1, "Johnny", permissions, itemsPurchased, purchasesDetails));

        // set target's private fields so that the synchronized code lines would work

        Field privateField = target.getClass().getDeclaredField("id");
        privateField.setAccessible(true);
        privateField.set(target, 123456);
        privateField.setAccessible(false);

        privateField = target.getClass().getDeclaredField("permissions");
        privateField.setAccessible(true);
        privateField.set(target, targetPermissions);
        privateField.setAccessible(false);
    }

    @Test
    void validatePermission_HavePermission() throws NoPermissionException {

        when(subscriber.havePermission(permission)).thenReturn(true);
        subscriber.validatePermission(permission);
    }

    @Test
    void validatePermission_NoPermission() {

        assertThrows(NoPermissionException.class, () -> subscriber.validatePermission(permission));
    }

    @Test
    void validateAtLeastOnePermission() throws NoPermissionException {

        when(subscriber.havePermission(permission)).thenReturn(false);
        when(subscriber.havePermission(adminPermission)).thenReturn(true);
        subscriber.validateAtLeastOnePermission(permission, adminPermission);
    }

    @Test
    void getAllStores() throws NoPermissionException {

        doNothing().when(subscriber).validatePermission(AdminPermission.getInstance());
        subscriber.getAllStores(stores);
    }

    @Test
    void addManagerPermission() throws NoPermissionException, AlreadyManagerException {

        doNothing().when(subscriber).validatePermission(ownerPermission);
        subscriber.addManagerPermission(target, store);
        verify(target).addPermission(managerPermission);
        verify(subscriber).addPermission(removePermissionPermission);
    }

    @Test
    void addManagerPermission_AlreadyManager() {

        when(subscriber.havePermission(ownerPermission)).thenReturn(true);
        when(target.havePermission(managerPermission)).thenReturn(true);
        assertThrows(AlreadyManagerException.class, () -> subscriber.addManagerPermission(target, store));
        verify(target, never()).addPermission(any());
        verify(subscriber, never()).addPermission(any());
    }

    @Test
    void addOwnerPermission() throws NoPermissionException, AlreadyOwnerException {

        doNothing().when(subscriber).validatePermission(ownerPermission);
        subscriber.addOwnerPermission(target, store);
        verify(target).addPermission(ownerPermission);
        verify(target).addPermission(managerPermission);
        verify(target).addPermission(manageInventoryPermission);
        verify(subscriber).addPermission(removePermissionPermission);
    }

    @Test
    void addOwnerPermission_AlreadyOwner() {

        when(subscriber.havePermission(ownerPermission)).thenReturn(true);
        when(target.havePermission(ownerPermission)).thenReturn(true);
        assertThrows(AlreadyOwnerException.class, () -> subscriber.addOwnerPermission(target, store));
        verify(target, never()).addPermission(any());
        verify(subscriber, never()).addPermission(any());
    }

    @Test
    void addOwnerPermission_ManagerAppointedByCaller() throws AlreadyOwnerException, NoPermissionException {

        when(subscriber.havePermission(ownerPermission)).thenReturn(true);
        when(subscriber.havePermission(removePermissionPermission)).thenReturn(true);
        when(target.havePermission(ownerPermission)).thenReturn(false);
        when(target.havePermission(managerPermission)).thenReturn(true);
        subscriber.addOwnerPermission(target, store);
        verify(target).addPermission(ownerPermission);
        verify(target).addPermission(managerPermission);
        verify(target).addPermission(manageInventoryPermission);
        verify(subscriber).addPermission(removePermissionPermission);
    }

    @Test
    void addOwnerPermission_ManagerAppointedByAnother() {

        when(subscriber.havePermission(ownerPermission)).thenReturn(true);
        when(target.havePermission(ownerPermission)).thenReturn(false);
        when(target.havePermission(managerPermission)).thenReturn(true);
        assertThrows(NoPermissionException.class, () -> subscriber.addOwnerPermission(target, store));
        verify(target, never()).addPermission(any());
        verify(subscriber, never()).addPermission(any());
    }

    @Test
    void removeOwnerPermission() throws NoPermissionException {

        when(subscriber.havePermission(removePermissionPermission)).thenReturn(true);
        subscriber.removeOwnerPermission(target, store);
        verify(target).removePermission(ownerPermission);
        verify(target).removePermission(manageInventoryPermission);
        verify(target).removePermission(managerPermission);
    }

    @Test
    void removeManagerPermission() throws NoPermissionException {

        doNothing().when(subscriber).validatePermission(removePermissionPermission);
        subscriber.removeManagerPermission(target, store);
        verify(target).removePermission(manageInventoryPermission);
        verify(target).removePermission(managerPermission);
    }

    @Test
    void addStoreItem() throws ItemException, NoPermissionException {

        String item = "PS5";
        String category = "Electronics";

        doNothing().when(subscriber).validatePermission(manageInventoryPermission);
        subscriber.addStoreItem(store, item, category, subCategory, quantity, price);
        verify(store).addItem(item, price, category, subCategory, quantity);
    }

    @Test
    void removeStoreItem() throws ItemException, NoPermissionException {

        doNothing().when(subscriber).validatePermission(manageInventoryPermission);
        subscriber.removeStoreItem(store, itemId);
        verify(store).removeItem(itemId);
    }

    @Test
    void updateStoreItem() throws ItemException, NoPermissionException {

        doNothing().when(subscriber).validatePermission(manageInventoryPermission);
        subscriber.updateStoreItem(store, itemId, subCategory, quantity, price);
        verify(store).changeItem(itemId, subCategory, quantity, price);
    }

    @Test
    void getEventLog() throws NoPermissionException {

        doNothing().when(subscriber).validatePermission(AdminPermission.getInstance());
        subscriber.getEventLog(null);
    }
}