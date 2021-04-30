package user;

import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Item;
import store.Store;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriberTest {

    private Subscriber subscriber;

    @Mock private Permission permission;
    @Mock private Set<Permission> permissions;
    @Mock private Set<Permission> targetPermissions;
    @Mock private Collection<Store> stores;
    @Mock private ConcurrentHashMap<Store, Collection<Item>> itemsPurchased;
    @Spy private final Collection<String> purchasesHistory = new LinkedList<>();
    @Spy private Item item;
    @Mock private Item item2;

    private final Store store = mock(Store.class);
    private final Subscriber target = mock(Subscriber.class);

    private final Permission adminPermission = AdminPermission.getInstance();
    private final Permission managerPermission = ManagerPermission.getInstance(store);
    private final Permission ownerPermission = OwnerPermission.getInstance(store);
    private final Permission manageInventoryPermission = ManageInventoryPermission.getInstance(store);
    private final Permission getHistoryPermission = GetHistoryPermission.getInstance(store);
    private final Permission editPolicyPermission = EditPolicyPermission.getInstance(store);
    private final Permission appointerPermission = AppointerPermission.getInstance(target, store);

    private final double price = 500.0;
    private final int quantity = 3;
    private final int itemId = 37373;
    private final String subCategory = "Gaming Consoles";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        subscriber = spy(new Subscriber(1, "Johnny", permissions, itemsPurchased, purchasesHistory));

        reset(store);
        reset(target);

        // set target's private fields so that the "synchronized" would not throw an exception

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
    void validatePermission_havePermission() throws NoPermissionException {

        when(subscriber.havePermission(permission)).thenReturn(true);
        subscriber.validatePermission(permission);
    }

    @Test
    void validatePermission_noPermission() {

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
        verify(subscriber).addPermission(appointerPermission);
    }

    @Test
    void addManagerPermission_alreadyManager() throws NoPermissionException {

        doNothing().when(subscriber).validatePermission(ownerPermission);
        when(target.havePermission(managerPermission)).thenReturn(true);
        assertThrows(AlreadyManagerException.class, () -> subscriber.addManagerPermission(target, store));
        verify(target, never()).addPermission(any());
        verify(subscriber, never()).addPermission(any());
    }

    @Test
    void addOwnerPermissions_toSelf() {

        subscriber.addOwnerPermission(store);

        verify(subscriber).addPermission(OwnerPermission.getInstance(store));
        verify(subscriber).addPermission(ManagerPermission.getInstance(store));
        verify(subscriber).addPermission(ManageInventoryPermission.getInstance(store));
        verify(subscriber).addPermission(GetHistoryPermission.getInstance(store));
        verify(subscriber).addPermission(EditPolicyPermission.getInstance(store));
    }

    @Test
    void addOwnerPermissions_toTarget() throws NoPermissionException, AlreadyOwnerException {

        doNothing().when(subscriber).validatePermission(ownerPermission);
        subscriber.addOwnerPermission(target, store);
        verify(target).addOwnerPermission(store);
        verify(subscriber).addPermission(appointerPermission);
    }

    @Test
    void addOwnerPermissions_targetIsAlreadyOwner() {

        when(subscriber.havePermission(ownerPermission)).thenReturn(true);
        when(target.havePermission(ownerPermission)).thenReturn(true);
        assertThrows(AlreadyOwnerException.class, () -> subscriber.addOwnerPermission(target, store));
        verify(target, never()).addPermission(any());
        verify(subscriber, never()).addPermission(any());
    }

    @Test
    void addOwnerPermissions_targetIsManagerAppointedByCaller() throws AlreadyOwnerException, NoPermissionException {

        when(subscriber.havePermission(ownerPermission)).thenReturn(true);
        when(subscriber.havePermission(appointerPermission)).thenReturn(true);
        when(target.havePermission(ownerPermission)).thenReturn(false);
        when(target.havePermission(managerPermission)).thenReturn(true);
        subscriber.addOwnerPermission(target, store);
        verify(target).addOwnerPermission(store);
        verify(subscriber).addPermission(appointerPermission);
    }

    @Test
    void addOwnerPermission_targetIsManagerAppointedByAnother() {

        when(subscriber.havePermission(ownerPermission)).thenReturn(true);
        when(target.havePermission(ownerPermission)).thenReturn(false);
        when(target.havePermission(managerPermission)).thenReturn(true);
        assertThrows(NoPermissionException.class, () -> subscriber.addOwnerPermission(target, store));
        verify(target, never()).addPermission(any());
        verify(subscriber, never()).addPermission(any());
    }

    @Test
    void removeOwnerPermission_fromTarget() throws NoPermissionException {

        doNothing().when(subscriber).validatePermission(appointerPermission);
        subscriber.removeOwnerPermission(target, store);
        verify(target).removeOwnerPermission(store);
    }

    @Test
    void removeOwnerPermission_fromSelf() throws NoSuchFieldException, IllegalAccessException {

        Set<Permission> permissions = new HashSet<>();

        Field privateField = subscriber.getClass().getDeclaredField("permissions");
        privateField.setAccessible(true);
        privateField.set(subscriber, permissions);
        privateField.setAccessible(false);

        subscriber.removeOwnerPermission(store);
        verify(subscriber).removePermission(ownerPermission);
        verify(subscriber).removePermission(manageInventoryPermission);
        verify(subscriber).removePermission(getHistoryPermission);
        verify(subscriber).removePermission(editPolicyPermission);
        verify(subscriber).removePermission(managerPermission);
    }

    @Test
    void removeOwnerPermission_fromSelf_recursive() throws NoSuchFieldException, IllegalAccessException {

        Set<Permission> permissions = new HashSet<>();
        permissions.add(appointerPermission);
        permissions.add(managerPermission);
        permissions.add(getHistoryPermission);
        permissions.add(editPolicyPermission);
        permissions.add(manageInventoryPermission);

        Field privateField = subscriber.getClass().getDeclaredField("permissions");
        privateField.setAccessible(true);
        privateField.set(subscriber, permissions);
        privateField.setAccessible(false);

        subscriber.removeOwnerPermission(store);
        verify(target).removeOwnerPermission(store);
        verify(subscriber).removeOwnerPermission(store);
    }

    @Test
    void removeManagerPermission() throws NoPermissionException {

        doNothing().when(subscriber).validatePermission(appointerPermission);
        subscriber.removeManagerPermission(target, store);
        verify(target).removeOwnerPermission(store);
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

    @Test
    void addPermissionToManager() throws TargetIsNotManagerException, NoPermissionException {

        doNothing().when(subscriber).validatePermission(appointerPermission);
        when(target.havePermission(managerPermission)).thenReturn(true);
        subscriber.addPermissionToManager(target, store, permission);
        verify(target).addPermission(permission);
    }

    @Test
    void addPermissionToManager_targetNotManager() throws NoPermissionException {

        doNothing().when(subscriber).validatePermission(appointerPermission);
        when(target.havePermission(managerPermission)).thenReturn(false);
        assertThrows(TargetIsNotManagerException.class, () -> subscriber.addPermissionToManager(target, store, permission));
        verify(target, never()).addPermission(any());
    }

    @Test
    void removePermissionFromManager() throws TargetIsOwnerException, NoPermissionException {

        doNothing().when(subscriber).validatePermission(appointerPermission);
        when(target.havePermission(ownerPermission)).thenReturn(false);
        subscriber.removePermissionFromManager(target, store, permission);
        verify(target).removePermission(permission);
    }

    @Test
    void removePermissionFromManager_targetIsOwner() throws NoPermissionException {

        doNothing().when(subscriber).validatePermission(appointerPermission);
        when(target.havePermission(ownerPermission)).thenReturn(true);
        assertThrows(TargetIsOwnerException.class, () -> subscriber.removePermissionFromManager(target, store, permission));
        verify(target, never()).removePermission(permission);
    }

    @Test
    void writeOpinionOnProductGoodDetails() throws ItemException, WrongReviewException {
        Collection<Item> items = new LinkedList<>();
        items.add(item);

        when(itemsPurchased.get(store)).thenReturn(items);
        when(store.searchItemById(0)).thenReturn(item);

        assertEquals(0, item.getReviews().size());
        subscriber.writeOpinionOnProduct(store, item.getId(), "good product");
        assertEquals(1, item.getReviews().size());
    }

    @Test
    void writeOpnionOnProductBadReviewDetails() {
        assertThrows(WrongReviewException.class, ()-> subscriber.writeOpinionOnProduct(store, item.getId(), null));
        assertThrows(WrongReviewException.class, ()-> subscriber.writeOpinionOnProduct(store, item.getId(), "    "));
    }

    @Test
    void writeOpnionOnProductNotPurchasedItem() throws ItemException {
        Collection<Item> items = new LinkedList<>();
        items.add(item);
        when(store.searchItemById(0)).thenReturn(item2);
        when(itemsPurchased.get(store)).thenReturn(items);

        assertThrows(ItemNotPurchasedException.class, ()-> subscriber.writeOpinionOnProduct(store, item2.getId(), "good product"));
        assertEquals(0, item.getReviews().size());
        assertEquals(0, item2.getReviews().size());
    }

    @Test
    void getPurchaseHistory() {
        assertEquals(0, subscriber.getPurchaseHistory().size());
        purchasesHistory.add("milk");
        purchasesHistory.add("cheese");
        assertEquals(2, subscriber.getPurchaseHistory().size());
        assertTrue(subscriber.getPurchaseHistory().contains("milk"));
        assertTrue(subscriber.getPurchaseHistory().contains("cheese"));
    }

    @Test
    void getSalesHistoryByStore() throws NoPermissionException {
        purchasesHistory.add("milk");
        purchasesHistory.add("cheese");
        when(store.getPurchaseHistory()).thenReturn(purchasesHistory);
        when(subscriber.havePermission(getHistoryPermission)).thenReturn(true);

        assertEquals(2, subscriber.getSalesHistoryByStore(store).size());
        assertTrue(subscriber.getSalesHistoryByStore(store).contains("milk"));
        assertTrue(subscriber.getSalesHistoryByStore(store).contains("cheese"));
    }

    @Test
    void getSalesHistoryByStoreNoPremission() {
        when(subscriber.havePermission(getHistoryPermission)).thenReturn(false);
        assertThrows(NoPermissionException.class, ()-> subscriber.getSalesHistoryByStore(store));
    }
}