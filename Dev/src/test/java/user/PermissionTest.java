package user;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import persistence.RepoMock;
import store.Store;

import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertNotSame;

public class PermissionTest {

    private final Store store = mock(Store.class);
    private StorePermission permission;
    private StorePermission differentClassPermission;

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
        permission = ManagerPermission.getInstance(store);
        differentClassPermission = OwnerPermission.getInstance(store);
    }

    @Test
    void testPermissionNotSame() {
        assertNotSame(permission, differentClassPermission);
    }
}