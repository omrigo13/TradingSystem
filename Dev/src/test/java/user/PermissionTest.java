package user;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import persistence.RepoMock;
import store.Store;

import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertNotSame;

public class PermissionTest {

    private final Store store = mock(Store.class);
    private final StorePermission permission = ManagerPermission.getInstance(store);
    private final StorePermission differentClassPermission = OwnerPermission.getInstance(store);

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
    }

    @Test
    void testPermissionNotSame() {
        assertNotSame(permission, differentClassPermission);
    }
}