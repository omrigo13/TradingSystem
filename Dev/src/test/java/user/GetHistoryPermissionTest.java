package user;

import org.junit.jupiter.api.Test;
import store.Store;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class GetHistoryPermissionTest {

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