package user;

public class Guest implements State {

    @Override
    public void login(User user, String userName, String password) throws LoginNonExistingSubscriberException {
        if (!user.userNames.contains(userName))
            throw new LoginNonExistingSubscriberException();
        user.changeState();
    }

    @Override
    public void logout() throws LogoutGuestException {
        throw  new LogoutGuestException();
    }

    @Override
    public void register(User user, String userName, String password) throws RegistrationException {
        if (user.userNames.contains(userName))
            throw new SubscriberAlreadyExistsException();
        user.userNames.add(userName);
    }
}
