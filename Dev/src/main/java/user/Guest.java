package user;

import authentication.LoginException;
import authentication.UserAlreadyExistsException;

public class Guest implements State {

    @Override
    public void login(User user, String userName, String password) throws LoginException {
        user.getUserAuthentication().login(userName, password);
        user.setUserName(userName);
        user.changeState(new Subscriber());
        user.getPersistence().retrieve(user);
    }

    @Override
    public void logout(User user) throws LogoutGuestException {
        throw new LogoutGuestException();
    }
}
