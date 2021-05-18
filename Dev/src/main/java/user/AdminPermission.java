package user;

import javax.persistence.*;
import java.lang.ref.WeakReference;

@Entity
public class AdminPermission extends Permission
{
    public AdminPermission() {
    }

    public static AdminPermission getInstance() {

        return (AdminPermission)pool.computeIfAbsent(new AdminPermission(), WeakReference::new).get();
    }

    @Override
    public String toString() {
        return "AdminPermission";
    }
}
