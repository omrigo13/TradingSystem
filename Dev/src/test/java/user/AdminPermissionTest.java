package user;

import org.testng.annotations.Test;

import static org.testng.Assert.assertSame;

public class AdminPermissionTest {

    private final Permission permission = AdminPermission.getInstance();
    private final Permission samePermission = AdminPermission.getInstance();

    @Test
    void testPermissionSame() {
        assertSame(permission, samePermission);
    }
}
