package exceptions;

public class SubscriberAlreadyExistsException extends InvalidActionException {
    final String userName;

    public SubscriberAlreadyExistsException(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "SubscriberAlreadyExistsException{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
