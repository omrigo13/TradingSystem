package user;

import java.lang.ref.WeakReference;

public class AdminPermission extends Permission
{
    private AdminPermission() {
    }

    public static AdminPermission getInstance() {

        return (AdminPermission)pool.computeIfAbsent(new AdminPermission(), WeakReference::new).get();
    }

    @Override
    public String toString() {
        return "AdminPermission";
    }
}
