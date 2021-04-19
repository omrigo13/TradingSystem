package user;

import org.junit.jupiter.api.Test;
import store.Store;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class AppointerPermissionTest {

    private final Store store = mock(Store.class);
    private final Store differentStore = mock(Store.class);
    private final Subscriber target = mock(Subscriber.class);
    private final Subscriber differentTarget = mock(Subscriber.class);

    private final StorePermission permission = AppointerPermission.getInstance(target, store);
    private final StorePermission samePermission = AppointerPermission.getInstance(target, store);
    private final StorePermission differentTargetPermission = AppointerPermission.getInstance(differentTarget, store);
    private final StorePermission differentStorePermission = AppointerPermission.getInstance(target, differentStore);
    private final StorePermission differentAllPermission = AppointerPermission.getInstance(differentTarget, differentStore);

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