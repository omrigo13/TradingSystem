package authentication;

import exceptions.SubscriberAlreadyExistsException;
import exceptions.SubscriberDoesNotExistException;
import exceptions.WrongPasswordException;

import java.util.Map;

// TODO need to be mock when we have real authentication system
public class UserAuthentication {

    private final Map<String, String> userNamesAndPasswords; // TODO need to consider concurrency

    public UserAuthentication(Map<String, String> userNamesAndPasswords) {
        this.userNamesAndPasswords = userNamesAndPasswords;
    }

    public void register(String userName, String password) throws SubscriberAlreadyExistsException {
        if (userNamesAndPasswords.putIfAbsent(userName, password) != null)
            throw new SubscriberAlreadyExistsException(userName);
    }

    public void authenticate(String userName, String password) throws SubscriberDoesNotExistException, WrongPasswordException {
        String currentPassword = userNamesAndPasswords.get(userName);
        if (currentPassword == null)
            throw new SubscriberDoesNotExistException(userName);
        if (!currentPassword.equals(password))
            throw new WrongPasswordException(userName, password);
    }
}
