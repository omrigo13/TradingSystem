package exceptions;

public class SubscriberDoesNotExistException extends InvalidActionException {
    final String userName;

    public SubscriberDoesNotExistException(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "SubscriberDoesNotExistException{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
