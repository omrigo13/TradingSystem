package authentication;

import java.util.HashMap;
import java.util.Map;

// TODO need to be mock when we have real authentication system
public class UserAuthentication {

    private final Map<String, String> userNamesAndPasswords = new HashMap<>(); // TODO need to consider concurrency

    public void register(String userName, String password) throws UserAlreadyExistsException {
        String exist = userNamesAndPasswords.putIfAbsent(userName, password);
        if(exist != null)
            throw new UserAlreadyExistsException();
    }

    public void login(String userName, String password) throws LoginException
    {
        if(!userNamesAndPasswords.containsKey(userName))
            throw new UserDoesNotExistException();
        if(!userNamesAndPasswords.get(userName).equals(password))
            throw new WrongPasswordException();
    }
}
