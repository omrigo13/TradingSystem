package exceptions;

public class NoPermissionException extends Exception {
    final String permission;

    public NoPermissionException(String permission) {
        this.permission = permission;
    }
}
