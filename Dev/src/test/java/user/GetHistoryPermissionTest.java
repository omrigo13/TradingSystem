package user;

import org.testng.annotations.Test;
import store.Store;

import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertNotSame;
import static org.testng.AssertJUnit.assertSame;

public class GetHistoryPermissionTest {

    private final Store store = mock(Store.class);
    private final Store differentStore = mock(Store.class);
    private final StorePermission permission = GetHistoryPermission.getInstance(store);
    private final StorePermission samePermission = GetHistoryPermission.getInstance(store);
    private final StorePermission differentPermission = GetHistoryPermission.getInstance(differentStore);

    @Test
    void testPermissionSame() {
        assertSame(permission, samePermission);
    }

    @Test
    void testPermissionNotSame() {
        assertNotSame(permission, differentPermission);
    }
}