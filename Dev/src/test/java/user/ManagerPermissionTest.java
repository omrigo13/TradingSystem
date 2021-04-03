package user;

import org.junit.jupiter.api.Test;
import store.Store;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class ManagerPermissionTest {

    private final Store store = mock(Store.class);
    private final Store differentStore = mock(Store.class);
    private final Permission permission = ManagerPermission.getInstance(store);
    private final Permission samePermission = ManagerPermission.getInstance(store);
    private final Permission differentPermission = ManagerPermission.getInstance(differentStore);

    @Test
    void testPermissionSame() {
        assertSame(permission, samePermission);
    }

    @Test
    void testPermissionNotSame() {
        assertNotSame(permission, differentPermission);
    }
}