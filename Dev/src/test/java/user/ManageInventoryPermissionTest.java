package user;

import org.testng.annotations.Test;
import store.Store;

import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertNotSame;
import static org.testng.AssertJUnit.assertSame;

public class ManageInventoryPermissionTest {

    private final Store store = mock(Store.class);
    private final Store differentStore = mock(Store.class);
    private final StorePermission permission = ManageInventoryPermission.getInstance(store);
    private final StorePermission samePermission = ManageInventoryPermission.getInstance(store);
    private final StorePermission differentPermission = ManageInventoryPermission.getInstance(differentStore);

    @Test
    void testPermissionSame() {
        assertSame(permission, samePermission);
    }

    @Test
    void testPermissionNotSame() {
        assertNotSame(permission, differentPermission);
    }
}