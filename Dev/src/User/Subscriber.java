package User;

public class Subscriber implements State {

    @Override
    public void login(User user, String userName, String password) throws LoginSubscriberAlreadyLoggedInException {
        throw new LoginSubscriberAlreadyLoggedInException();
    }

    @Override
    public void logout() {

    }

    @Override
    public void register(User user, String userName, String password) throws RegistrationException {
        throw new SubscriberAlreadyExistsException();
    }
}
