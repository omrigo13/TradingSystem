package user;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import persistence.RepoMock;

import static org.testng.Assert.assertSame;

public class AdminPermissionTest {

    private Permission permission;
    private Permission samePermission;

    @BeforeClass
    public void beforeClass() {
        RepoMock.enable();
        permission = AdminPermission.getInstance();
        samePermission = AdminPermission.getInstance();
    }

    @Test
    void testPermissionSame() {
        assertSame(permission, samePermission);
    }
}
