package exceptions;

public class AlreadyManagerException extends Exception {
    final String userName;

    public AlreadyManagerException(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "AlreadyManagerException{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
