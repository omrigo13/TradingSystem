package persistence;

import user.Basket;
import user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// TODO need to be mock when we have real authentication system
public class Carts {

    private final Map<String, Collection<Basket>> carts = new HashMap<>();

    public void persist(User user)
    {
        carts.putIfAbsent(user.getUserName(), user.getCart());
    }

    public void retrieve(User user) {
        user.setBaskets(carts.get(user.getUserName()));
    }
}
