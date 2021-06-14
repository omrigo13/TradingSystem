package user;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import persistence.RepoMock;
import store.Store;

import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertNotSame;
import static org.testng.AssertJUnit.assertSame;

public class OwnerPermissionTest {

    private final Store store = mock(Store.class);
    private final Store differentStore = mock(Store.class);
    private StorePermission permission;
    private StorePermission samePermission;
    private StorePermission differentPermission;

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
        permission = OwnerPermission.getInstance(store);
        samePermission = OwnerPermission.getInstance(store);
        differentPermission = OwnerPermission.getInstance(differentStore);
    }

    @Test
    void testPermissionSame() {
        assertSame(permission, samePermission);
    }

    @Test
    void testPermissionNotSame() {
        assertNotSame(permission, differentPermission);
    }
}