package user;

import authentication.LoginException;
import authentication.UserAuthentication;
import persistence.Carts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserImpl implements User {

    private final Carts persistence;
    private String userName;
    private State state;
    private Map<String, Basket> baskets = new HashMap<>();
    private final UserAuthentication userAuthentication;

    public UserImpl(UserAuthentication userAuthentication, Carts persistence)
    {
        this.userAuthentication = userAuthentication;
        this.state = new Guest();
        this.persistence = persistence;
    }

    @Override
    public Carts getPersistence() {
        return persistence;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public void setBaskets(Collection<Basket> baskets) {
        this.baskets = new HashMap<>();
        if(baskets != null) {
            for (Basket basket : baskets) {
                this.baskets.put(basket.getStore(), basket);
            }
        }
    }

    @Override
    public UserAuthentication getUserAuthentication() {
        return userAuthentication;
    }

    @Override
    public void login(String userName, String password) throws LoginException
    {
        state.login(this, userName, password);
    }

    @Override
    public void logout() throws LogoutGuestException {
        state.logout(this);
    }

    @Override
    public void changeState(State state)
    {
        this.state = state;
    }

    @Override
    public Basket getBasket(String storeID)
    {
        Basket basket = baskets.get(storeID);
        if (basket == null) {
            basket = new Basket(storeID, this);
            baskets.put(storeID, basket);
        }
        return basket;
    }

    @Override
    public Collection<Basket> getCart()
    {
        return baskets.values();
    }
}
