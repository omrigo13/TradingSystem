package exceptions;

public class AlreadyManagerException extends InvalidActionException {

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
