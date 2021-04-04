package user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class AdminPermissionTest {

    private final Permission permission = AdminPermission.getInstance();
    private final Permission samePermission = AdminPermission.getInstance();

    @Test
    void testPermissionSame() {
        assertSame(permission, samePermission);
    }
}
