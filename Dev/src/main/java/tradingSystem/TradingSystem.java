package tradingSystem;

import authentication.UserAuthentication;
import exceptions.*;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import store.Item;
import store.Store;
import user.*;

import java.util.*;

public class TradingSystem {

    private int storeIdCounter = 1;

    private final DeliverySystem deliverySystem;
    private final PaymentSystem paymentSystem;
    private final UserAuthentication auth;
    private final Map<String, Subscriber> subscribers; // key: user name
    private final Map<String, User> connections; // key: connection id
    private final Map<Integer, Store> stores; // key: store id

    private TradingSystem(String userName, String password, PaymentSystem paymentSystem, DeliverySystem deliverySystem,
                          UserAuthentication auth, Map<String, Subscriber> subscribers, Map<String, User> connections,
                          Map<Integer, Store> stores) throws LoginException {
        this.paymentSystem = paymentSystem;
        this.deliverySystem = deliverySystem;
        this.auth = auth;
        this.subscribers = subscribers;
        this.connections = connections;
        this.stores = stores;
        try {
            auth.authenticate(userName, password); // TODO check if the userName is admin
        } catch (SubscriberDoesNotExistException e) {
            throw new LoginException(e);
        } catch (WrongPasswordException e) {
            throw new LoginException(e);
        }
    }

    public static TradingSystem createTradingSystem(String userName, String password, PaymentSystem paymentSystem, DeliverySystem deliverySystem,
                                                    UserAuthentication auth, Map<String, Subscriber> subscribers, Map<String, User> connections,
                                                    Map<Integer, Store> stores) throws LoginException {
        return new TradingSystem(userName, password, paymentSystem, deliverySystem, auth, subscribers, connections, stores);
    }

    public User getUserByConnectionId(String connectionId) throws ConnectionIdDoesNotExistException {
        User user = connections.get(connectionId);
        if (user == null)
            throw new ConnectionIdDoesNotExistException(connectionId);
        return user;
    }

    public Subscriber getSubscriberByConnectionId(String connectionId) throws ConnectionIdDoesNotExistException, NotLoggedInException {
        return getUserByConnectionId(connectionId).getSubscriber();
    }

    public Subscriber getSubscriberByUserName(String userName) throws SubscriberDoesNotExistException {
        Subscriber subscriber = subscribers.get(userName);
        if (subscriber == null)
            throw new SubscriberDoesNotExistException(userName);
        return subscriber;
    }

    public Collection<Store> getStores() {
        return stores.values();
    }

    public Store getStore(int storeId) {
        return stores.get(storeId);
    }

    public void register(String userName, String password) throws SubscriberAlreadyExistsException {
        auth.register(userName, password);
        subscribers.put(userName, new Subscriber(userName, new HashMap<>(), new HashSet<>()));
    }

    public String connect()
    {
        String connectionId = java.util.UUID.randomUUID().toString();
        // if need to be sticklers about uniqueness switch to org.springframework.util.AlternativeJdkIdGenerator

        connections.put(connectionId, new User(new HashMap<>()));
        return connectionId;
    }

    public void login(String connectionId, String userName, String password) throws LoginException {

        try {
            User user = getUserByConnectionId(connectionId);
            auth.authenticate(userName, password);
            User subscriber = getSubscriberByUserName(userName);
            subscriber.makeCart(user);
            connections.put(connectionId, subscriber);

        } catch (ConnectionIdDoesNotExistException e) {
            throw new LoginException(e);
        } catch (SubscriberDoesNotExistException e) {
            throw new LoginException(e);
        } catch (WrongPasswordException e) {
            throw new LoginException(e);
        }
    }

    public void logout(String connectionId) throws ConnectionIdDoesNotExistException {
        User user = getUserByConnectionId(connectionId);
        User guest = new User(new HashMap<>());
        guest.makeCart(user);
        connections.put(connectionId, guest);
    }

    public int newStore(Subscriber subscriber, String storeName) throws NewStoreException {

        // create the new store
        Store store;
        try {
            store = new Store(storeIdCounter, storeName, "description");

        } catch (Exception e) {
            throw new NewStoreException(storeName, e);
        }
        stores.put(storeIdCounter, store);

        // give the subscriber owner permission
        subscriber.addPermission(OwnerPermission.getInstance(store));
        subscriber.addPermission(ManagerPermission.getInstance(store));
        subscriber.addPermission(ManageInventoryPermission.getInstance(store));

        return storeIdCounter++;
    }

    public Collection<Subscriber> getStoreStaff(Subscriber subscriber, Store store, Collection<Subscriber> staff) throws NoPermissionException {

        subscriber.validateAtLeastOnePermission(AdminPermission.getInstance(), ManagerPermission.getInstance(store));

        Permission managerPermission = ManagerPermission.getInstance(store);
        for (Subscriber potentialStaff : subscribers.values())
            if (potentialStaff.havePermission(managerPermission))
                staff.add(potentialStaff);

        return staff;
    }

    public Collection<String> getItems(String keyWord, String productName, String category, String subCategory,
                                       Double ratingItem, Double ratingStore, Double maxPrice, Double minPrice) {

        Collection<String> items = new LinkedList<>();
        Collection<Item> itemsToAdd;
        for (Store store : stores.values()) {
            itemsToAdd = store.searchAndFilter(keyWord, productName, category, ratingItem, ratingStore, maxPrice, minPrice);
            for (Item item : itemsToAdd)
                items.add(item.toString());
        }
        return items;
    }
}
