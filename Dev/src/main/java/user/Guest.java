package user;

import tradingSystem.SubscriberAlreadyExistsException;

public class Guest implements State {

    @Override
    public void login(User user, String userName, String password) throws LoginNonExistingSubscriberException {
        if (!user.getUserNames().contains(userName))
            throw new LoginNonExistingSubscriberException();
        user.changeState(new Subscriber());
    }

    @Override
    public void logout(User user) throws LogoutGuestException {
        throw new LogoutGuestException();
    }

    @Override
    public void register(User user, String userName, String password) throws SubscriberAlreadyExistsException {
        if (user.getUserNames().contains(userName))
            throw new SubscriberAlreadyExistsException();
        user.getUserNames().add(userName);
    }
}
