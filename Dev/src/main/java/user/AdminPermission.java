package user;

import javax.persistence.Entity;

@Entity
public class AdminPermission extends Permission
{
    public AdminPermission() {
    }

    public static AdminPermission getInstance() {
        return getInstance(new AdminPermission());
    }

    @Override
    public String toString() {
        return "AdminPermission";
    }
}
