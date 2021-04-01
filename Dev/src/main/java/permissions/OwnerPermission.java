package permissions;

import store.Store;

public class OwnerPermission extends Permission
{
    public OwnerPermission(Store store) {
        super(store);
    }
}
