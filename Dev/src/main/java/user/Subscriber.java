package user;

import authentication.UserAlreadyExistsException;

public class Subscriber implements State {

    @Override
    public void login(User user, String userName, String password) throws SubscriberAlreadyLoggedInException {
        throw new SubscriberAlreadyLoggedInException();
    }

    @Override
    public void logout(User user) {
        user.getPersistence().persist(user);
        user.changeState(new Guest());
    }

}
