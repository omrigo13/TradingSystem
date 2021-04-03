package user;

import org.junit.jupiter.api.Test;
import store.Store;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class RemovePermissionPermissionTest {

    private final Store store = mock(Store.class);
    private final Store differentStore = mock(Store.class);
    private final Subscriber target = mock(Subscriber.class);
    private final Subscriber differentTarget = mock(Subscriber.class);

    private final StorePermission permission = RemovePermissionPermission.getInstance(target, store);
    private final StorePermission samePermission = RemovePermissionPermission.getInstance(target, store);
    private final StorePermission differentTargetPermission = RemovePermissionPermission.getInstance(differentTarget, store);
    private final StorePermission differentStorePermission = RemovePermissionPermission.getInstance(target, differentStore);
    private final StorePermission differentAllPermission = RemovePermissionPermission.getInstance(differentTarget, differentStore);

    @Test
    void testPermissionSame() {
        assertSame(permission, samePermission);
    }

    @Test
    void testPermissionDifferentTarget() {
        assertNotSame(permission, differentTargetPermission);
    }

    @Test
    void testPermissionDifferentStore() {
        assertNotSame(permission, differentStorePermission);
    }

    @Test
    void testPermissionDifferentAll() {
        assertNotSame(permission, differentAllPermission);
    }
}