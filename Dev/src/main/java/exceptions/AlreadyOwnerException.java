package exceptions;

public class AlreadyOwnerException extends InvalidActionException {

    final String userName;

    public AlreadyOwnerException(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "AlreadyOwnerException{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
