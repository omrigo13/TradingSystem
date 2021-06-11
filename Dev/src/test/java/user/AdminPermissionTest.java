package user;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import persistence.RepoMock;

import static org.testng.Assert.assertSame;

public class AdminPermissionTest {

    private final Permission permission = AdminPermission.getInstance();
    private final Permission samePermission = AdminPermission.getInstance();

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
    }

    @Test
    void testPermissionSame() {
        assertSame(permission, samePermission);
    }
}
