package user;

import permissions.Permission;
import store.Store;

import java.util.Collection;
import java.util.Map;

public class Subscriber extends User {

    private final String userName;
    private final Collection<Permission> permissions;

    public Subscriber(String userName, Map<Store, Basket> baskets, Collection<Permission> permissions) {
        super(baskets);
        this.userName = userName;
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public void deletePermission(Permission permission) {
        permissions.remove(permission);
    }

    public boolean havePermission(Permission permission) {
        return permissions.contains(permission);
    }

    public void clearPermissions() {
        permissions.clear();
    }

    @Override
    public Subscriber getSubscriber() {
        return this;
    }
}
