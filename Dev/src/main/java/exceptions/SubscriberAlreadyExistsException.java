package exceptions;

public class SubscriberAlreadyExistsException extends Exception {
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
