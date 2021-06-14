package user;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import persistence.RepoMock;
import store.Store;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;

public class AppointerPermissionTest {

    private final Store store = mock(Store.class);
    private final Store differentStore = mock(Store.class);
    private final Subscriber target = mock(Subscriber.class);
    private final Subscriber differentTarget = mock(Subscriber.class);

    private StorePermission permission;
    private StorePermission samePermission;
    private StorePermission differentTargetPermission;
    private StorePermission differentStorePermission;
    private StorePermission differentAllPermission;

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
        permission = AppointerPermission.getInstance(target, store);
        samePermission = AppointerPermission.getInstance(target, store);
        differentTargetPermission = AppointerPermission.getInstance(differentTarget, store);
        differentStorePermission = AppointerPermission.getInstance(target, differentStore);
        differentAllPermission = AppointerPermission.getInstance(differentTarget, differentStore);
    }
    
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