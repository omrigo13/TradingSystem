package user;

import java.lang.ref.WeakReference;

public class AdminPermission extends Permission
{
    private AdminPermission() {
    }

    public static AdminPermission getInstance() {

        AdminPermission key = new AdminPermission();
        return (AdminPermission)pool.computeIfAbsent(key, k -> new WeakReference<>(key)).get();
    }

    @Override
    public String toString() {
        return "AdminPermission";
    }
}
