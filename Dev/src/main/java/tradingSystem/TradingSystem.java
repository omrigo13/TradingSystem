package tradingSystem;

import authentication.*;
import externalServices.DeliverySystem;
import externalServices.DeliverySystemMock;
import externalServices.PaymentSystem;
import externalServices.PaymentSystemMock;
import persistence.Carts;
import user.Basket;
import user.LogoutGuestException;
import user.User;
import user.UserImpl;

import java.util.*;

import static user.Basket.*;

public class TradingSystem {

    private final DeliverySystem deliverySystem;
    private final PaymentSystem paymentSystem;
    private final Carts persistence = new Carts();
    private final UserAuthentication auth;
    private final Map<String, User> activeUsers = new HashMap<>();
    private final Collection<String> stores = new HashSet<>();
    private static int guestID = 0;

    public TradingSystem(String userName, String password, PaymentSystem paymentSystem, DeliverySystem deliverySystem, UserAuthentication auth) throws LoginException {
        this.paymentSystem = paymentSystem;
        this.deliverySystem = deliverySystem;
        this.auth = auth;
        auth.login(userName, password);
        // TODO check if the userName is admin
    }

    public void register(String userName, String password) throws UserAlreadyExistsException {
        auth.register(userName, password);
    }

    public User getUser(String userID) throws UserIdDoesNotExistException {
        User user = activeUsers.get(userID);
        if(user == null)
            throw new UserIdDoesNotExistException();
        return user;
    }

//    public void login(String userID, String userName, String password) throws LoginException {
//        activeUsers.get(userID).login(userName, password);
//    }
//
//    public void logout(String userID) throws LogoutGuestException {
//        activeUsers.get(userID).logout();
//    }

//    public void addItemToBasket(String userID, String storeID, String item, int amount) throws UserDoesNotExistException {
//        // TODO check if the item exists in the inventory of the store
//        // TODO to lock the item in the inventory
//        User user = activeUsers.get(userID);
//
//        Basket basket = getUserBasket(userID, storeID);
//        basket.addItem(new ItemRecord(item, amount));
//    }
//
//    public Basket getUserBasket(String userID, String storeID) throws UserDoesNotExistException {
//        User user = activeUsers.get(userID);
//        if(user == null)
//            throw new UserDoesNotExistException();
//        return user.getBasket(storeID);
//    }

    public String connectGuest()
    {
        String guestID = "" + TradingSystem.guestID++; // TODO real implementation of random and unique ID
        activeUsers.put(guestID, new UserImpl(auth, persistence));
        return guestID;
    }

//    public Collection<String> getBasket(String userID, String storeID) throws UserDoesNotExistException {
//        List<String> list = new LinkedList<>();
//        User user = activeUsers.get(userID);
//        if(user == null)
//            throw new UserDoesNotExistException();
//        Basket basket  = user.getBasket(storeID);
//        for (ItemRecord item : basket.getItems()) {
//            list.add(item.toString());
//        }
//        return list;
//    }
//
//    public Collection<String> getStores(String userID) throws UserDoesNotExistException {
//        List<String> list = new LinkedList<>();
//        User user = activeUsers.get(userID);
//        if(user == null)
//            throw new UserDoesNotExistException();
//        for (Basket basket : user.getCart()) {
//            list.add(basket.getStore());
//        }
//        return list;
//    }
}
