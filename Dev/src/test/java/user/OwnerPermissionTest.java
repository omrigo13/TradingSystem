package user;

import org.junit.jupiter.api.Test;
import store.Store;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class OwnerPermissionTest {

    private final Store store = mock(Store.class);
    private final Store differentStore = mock(Store.class);
    private final Permission permission = OwnerPermission.getInstance(store);
    private final Permission samePermission = OwnerPermission.getInstance(store);
    private final Permission differentPermission = OwnerPermission.getInstance(differentStore);

    @Test
    void testPermissionSame() {
        assertSame(permission, samePermission);
    }

    @Test
    void testPermissionNotSame() {
        assertNotSame(permission, differentPermission);
    }
}