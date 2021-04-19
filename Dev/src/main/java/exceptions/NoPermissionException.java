package exceptions;

public class NoPermissionException extends InvalidActionException {
    final String permission;

    public NoPermissionException(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "NoPermissionException{" +
                "permission='" + permission + '\'' +
                '}';
    }
}
