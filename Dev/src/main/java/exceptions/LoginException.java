package exceptions;

public class LoginException extends Exception {
    public LoginException(SubscriberDoesNotExistException cause) {
        super(cause);
    }
    public LoginException(WrongPasswordException cause) {
        super(cause);
    }
    public LoginException(ConnectionIdDoesNotExistException cause) {
        super(cause);
    }
}
