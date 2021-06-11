package user;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import persistence.RepoMock;
import store.Store;

import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertNotSame;
import static org.testng.AssertJUnit.assertSame;

public class EditPolicyPermissionTest {

    private final Store store = mock(Store.class);
    private final Store differentStore = mock(Store.class);
    private final StorePermission permission = EditPolicyPermission.getInstance(store);
    private final StorePermission samePermission = EditPolicyPermission.getInstance(store);
    private final StorePermission differentPermission = EditPolicyPermission.getInstance(differentStore);

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
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