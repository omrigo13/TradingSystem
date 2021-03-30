package tradingSystem;

import authentication.*;
import persistence.Carts;
import user.Basket;
import user.LogoutGuestException;
import user.User;
import user.UserImpl;

import java.util.*;

import static user.Basket.*;

public class TradingSystem {

    private final Carts persistence = new Carts();
    private final UserAuthentication auth = new UserAuthentication();
    private final Map<String, User> activeUsers = new HashMap<>();
    private final Collection<String> stores = new HashSet<>();
    private static int guestID = 0;

    public void register(String userName, String password) throws UserAlreadyExistsException {
        auth.register(userName, password);
    }

    public void login(String userID, String userName, String password) throws LoginException {
        activeUsers.get(userID).login(userName, password);
    }

    public void logout(String userID) throws LogoutGuestException {
        activeUsers.get(userID).logout();
    }

    public void addItemToBasket(String userID, String storeID, String item, int amount) throws UserDoesNotExistException {
        // TODO check if the item exists in the inventory of the store
        // TODO to lock the item in the inventory
        Basket basket = getUserBasket(userID, storeID);
        basket.addItem(new ItemRecord(item, amount));
    }

    public Basket getUserBasket(String userID, String storeID) throws UserDoesNotExistException {
        User user = activeUsers.get(userID);
        if(user == null)
            throw new UserDoesNotExistException();
        return user.getBasket(storeID);
    }

    public String connectGuest()
    {
        String guest = "" + guestID++; // TODO real implementation of random and unique ID
        activeUsers.put(guest,new UserImpl(auth, persistence));
        return guest;
    }

    public Collection<String> getBasket(String userID, String storeID) throws UserDoesNotExistException {
        List<String> list = new LinkedList<>();
        User user = activeUsers.get(userID);
        if(user == null)
            throw new UserDoesNotExistException();
        Basket basket  = user.getBasket(storeID);
        for (ItemRecord item : basket.getItems()) {
            list.add(item.toString());
        }
        return list;
    }

    public Collection<String> getStores(String userID) throws UserDoesNotExistException {
        List<String> list = new LinkedList<>();
        User user = activeUsers.get(userID);
        if(user == null)
            throw new UserDoesNotExistException();
        for (Basket basket : user.getCart()) {
            list.add(basket.getStore());
        }
        return list;
    }
}
