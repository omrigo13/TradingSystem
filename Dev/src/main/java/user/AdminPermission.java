package user;

import java.util.Objects;

public class AdminPermission extends Permission
{
    private AdminPermission() {
    }

    public static AdminPermission getInstance() {
        int hash = Objects.hash(AdminPermission.class);
        AdminPermission permission = (AdminPermission)permissions.get(hash);
        if (permission == null) {
            permission = new AdminPermission();
            permissions.put(hash, permission);
        }
        return permission;
    }

    @Override
    public String toString() {
        return "AdminPermission";
    }
}
