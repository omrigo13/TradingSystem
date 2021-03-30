package user;

import tradingSystem.RegistrationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class User {

    private State state;
    private Map<String, Basket> baskets = new HashMap<>();
    private Collection<String> userNames;

    public User(Collection<String> userNames)
    {
        this.userNames = userNames;
        this.state = new Guest();
    }

    public Collection<String> getUserNames() {
        return userNames;
    }

    public void login(String userName, String password) throws LoginException
    {
        state.login(this, userName, password);
    }

    public void logout() throws LogoutGuestException {
        state.logout(this);
    }

    public void register(String userName, String password) throws RegistrationException
    {
        state.register(this, userName, password);
    }

    public void changeState(State state)
    {
        this.state = state;
    }

    public Basket getBasket(String storeID)
    {
        Basket basket = baskets.get(storeID);
        if (basket == null) {
            basket = new Basket(storeID, this);
            baskets.put(storeID, basket);
        }
        return basket;
    }

    public Collection<Basket> getCart()
    {
        return baskets.values();
    }
}
