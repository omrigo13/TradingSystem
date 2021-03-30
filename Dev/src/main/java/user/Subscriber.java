package user;

import tradingSystem.SubscriberAlreadyExistsException;

public class Subscriber implements State {

    @Override
    public void login(User user, String userName, String password) throws SubscriberAlreadyLoggedInException {
        throw new SubscriberAlreadyLoggedInException();
    }

    @Override
    public void logout(User user) {
        user.changeState(new Guest());
    }

    @Override
    public void register(User user, String userName, String password) throws SubscriberAlreadyExistsException {
        throw new SubscriberAlreadyExistsException();
    }
}
