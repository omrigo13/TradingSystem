package user;

public interface State {

    void login(User user, String userName, String password) throws LoginException;

    void logout() throws LogoutGuestException;

    void register(User user, String userName, String password) throws RegistrationException;

}
