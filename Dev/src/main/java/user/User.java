package user;

import java.util.Collection;

public class User {

    private State state;
    private Collection<Basket> baskets;

    Collection<String> userNames;

    public User(Collection<String> userNames)
    {
        this.userNames = userNames;
        this.state = new Guest();
        //this.baskets = new Collection<user.Basket>();
    }

    public void login(String userName, String password) throws LoginException
    {
        state.login(this, userName, password);
    }

    public void logout() throws LogoutGuestException {
        state.logout();
    }

    public void register (String userName, String password) throws RegistrationException
    {
        state.register(this, userName, password);
        changeState();
    }

    public void changeState()
    {
        this.state = new Subscriber();
    }

    public Basket getBasket(String storeID)
    {
        // find basket for storeID and return the basket
        return null;
    }

    public Collection<Basket> getCart()
    {
        return this.baskets;
    }



}
