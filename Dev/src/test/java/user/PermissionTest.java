package user;

import org.junit.jupiter.api.Test;
import store.Store;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.mockito.Mockito.mock;

class PermissionTest {

    private final Store store = mock(Store.class);
    private final Permission permission = ManagerPermission.getInstance(store);
    private final Permission differentClassPermission = OwnerPermission.getInstance(store);

    @Test
    void testPermissionNotSame() {
        assertNotSame(permission, differentClassPermission);
    }
}