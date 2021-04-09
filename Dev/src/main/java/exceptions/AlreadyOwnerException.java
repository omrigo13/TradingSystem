package exceptions;

public class AlreadyOwnerException extends Exception {

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
