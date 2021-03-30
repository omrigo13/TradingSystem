package tradingSystem;

import user.Basket;
import user.User;

import java.util.*;

import static user.Basket.*;

public class TradingSystem {

    private final Collection<String> userNames = new HashSet<>();
    private final Map<String, User> users = new HashMap<>();
    private final Collection<String> stores = new HashSet<>();
    private static int guestID = 0;

    public void register(String userName, String password) throws SubscriberAlreadyExistsException {
        if(userNames.contains(userName))
            throw new SubscriberAlreadyExistsException();
        userNames.add(userName);
    }

    public void addItemToBasket(String userID, String storeID, String item, int amount) throws UserDoesNotExistException {
        // TODO check if the item exists in the inventory of the store
        // TODO to lock the item in the inventory
        Basket basket = getUserBasket(userID, storeID);
        basket.addItem(new ItemRecord(item, amount));
    }

    public Basket getUserBasket(String userID, String storeID) throws UserDoesNotExistException {
        User user = users.get(userID);
        if(user == null)
            throw new UserDoesNotExistException();
        return user.getBasket(storeID);
    }

    public String connectGuest()
    {
        String guest = "" + guestID++;
        users.put(guest,new User(userNames));
        return guest;
    }

    public Collection<String> getBasket(String userID, String storeID) throws UserDoesNotExistException {
        List<String> list = new LinkedList<>();
        User user = users.get(userID);
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
        User user = users.get(userID);
        if(user == null)
            throw new UserDoesNotExistException();
        for (Basket basket : user.getCart()) {
            list.add(basket.getStore());
        }
        return list;
    }
}
