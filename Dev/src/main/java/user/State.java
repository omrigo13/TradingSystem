package user;

import authentication.LoginException;
import authentication.RegistrationException;

public interface State {

    void login(User user, String userName, String password) throws LoginException;

    void logout(User user) throws LogoutGuestException;
}
