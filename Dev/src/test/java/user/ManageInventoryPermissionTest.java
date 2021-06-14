package user;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import persistence.RepoMock;
import store.Store;

import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertNotSame;
import static org.testng.AssertJUnit.assertSame;

public class ManageInventoryPermissionTest {

    private final Store store = mock(Store.class);
    private final Store differentStore = mock(Store.class);
    private StorePermission permission;
    private StorePermission samePermission;
    private StorePermission differentPermission;

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
        permission = ManageInventoryPermission.getInstance(store);
        samePermission = ManageInventoryPermission.getInstance(store);
        differentPermission = ManageInventoryPermission.getInstance(differentStore);
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