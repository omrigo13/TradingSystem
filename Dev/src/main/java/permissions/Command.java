package permissions;

import user.Subscriber;

public abstract class Command {
    protected final Subscriber user;
    protected final Permission requiredPermission;

    public Command(Permission requiredPermission, Subscriber user) {
        this.requiredPermission = requiredPermission;
        this.user = user;
    }

    public Permission getRequiredPermission() {
        return requiredPermission;
    }

    abstract void execute() throws Exception; // TODO remove exception
}
