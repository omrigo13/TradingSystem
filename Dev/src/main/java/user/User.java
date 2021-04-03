package user;

import exceptions.NotLoggedInException;
import store.Store;

import java.util.HashMap;
import java.util.Map;

public class User {
    private final Map<Store, Basket> baskets;

    public User(Map<Store, Basket> baskets) {
        this.baskets = baskets;
    }

    public Map<Store, Basket>  getCart()
    {
        return baskets;
    }

    public void makeCart(User from)
    {
        if (baskets.isEmpty())
            baskets.putAll(from.getCart());
    }

    public Subscriber getSubscriber() throws NotLoggedInException {
        throw new NotLoggedInException();
    }

    public Basket getBasket(Store store) {

        Basket basket = baskets.get(store);
        if (basket == null) {
            basket = new Basket(store, this, new HashMap<>());
            baskets.put(store, basket);
        }
        return basket;
    }
}
