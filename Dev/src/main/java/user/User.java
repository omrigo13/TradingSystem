package user;

import exceptions.NotLoggedInException;
import purchaseAndReview.Purchase;
import store.Store;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class User {

    private final Map<Store, Basket> baskets;
    private final Collection<Purchase> purchases;

    public User() {
        this(new HashMap<>(), new LinkedList<>());
    }

    User(Map<Store, Basket> baskets, Collection<Purchase> purchases) {
        this.baskets = baskets;
        this.purchases = purchases;
    }

    public Map<Store, Basket> getCart()
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

    public void addPurchase(Purchase purchase) {purchases.add(purchase); }

    public void resetCart() {baskets.clear(); }

    public Collection<Purchase> getPurchases() {return purchases; }
}
