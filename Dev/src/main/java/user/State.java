package user;

import tradingSystem.RegistrationException;

public interface State {

    void login(User user, String userName, String password) throws LoginException;

    void logout(User user) throws LogoutGuestException;

    void register(User user, String userName, String password) throws RegistrationException;
}
