package tradingSystem;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import exceptions.InvalidConnectionIdException;
import exceptions.InvalidStoreIdException;
import exceptions.SubscriberDoesNotExistException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import purchasePolicy.defaultPurchasePolicy;
import store.Item;
import store.Store;
import user.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TradingSystem {

    private final AtomicInteger storeIdCounter = new AtomicInteger();
    private final AtomicInteger subscriberIdCounter;

    private final DeliverySystem deliverySystem;
    private final PaymentSystem paymentSystem;
    private final UserAuthentication auth;

    private final ConcurrentHashMap<String, Subscriber> subscribers; // key: user name
    private final ConcurrentHashMap<Integer, Store> stores; // key: store id
    private final ConcurrentHashMap<String, User> connections; // key: connection id

    TradingSystem(String userName, String password, AtomicInteger subscriberIdCounter, PaymentSystem paymentSystem, DeliverySystem deliverySystem,
                  UserAuthentication auth, ConcurrentHashMap<String, Subscriber> subscribers, ConcurrentHashMap<String, User> connections,
                  ConcurrentHashMap<Integer, Store> stores)
            throws InvalidActionException {

        this.subscriberIdCounter = subscriberIdCounter;
        this.paymentSystem = paymentSystem;
        this.deliverySystem = deliverySystem;
        this.auth = auth;
        this.subscribers = subscribers;
        this.connections = connections;
        this.stores = stores;

        auth.authenticate(userName, password);
        subscribers.get(userName).validatePermission(AdminPermission.getInstance());
    }

    public User getUserByConnectionId(String connectionId) throws InvalidActionException {

        User user = connections.get(connectionId);
        if (user == null)
            throw new InvalidConnectionIdException(connectionId);
        return user;
    }

    public Subscriber getSubscriberByUserName(String userName) throws InvalidActionException {

        Subscriber subscriber = subscribers.get(userName);
        if (subscriber == null)
            throw new SubscriberDoesNotExistException(userName);
        return subscriber;
    }

    public Collection<Store> getStores() {
        return stores.values();
    }

    public Store getStore(int storeId) throws InvalidStoreIdException {

        Store store = stores.get(storeId);
        if (store == null)
            throw new InvalidStoreIdException(storeId);
        return store;
    }

    public void register(String userName, String password) throws InvalidActionException {
        auth.register(userName, password);
        subscribers.put(userName, new Subscriber(subscriberIdCounter.getAndIncrement(), userName));
    }

    public String connect() {

        String connectionId = java.util.UUID.randomUUID().toString();
        // if need to be sticklers about uniqueness switch to org.springframework.util.AlternativeJdkIdGenerator

        connections.put(connectionId, new User());
        return connectionId;
    }

    public void login(String connectionId, String userName, String password) throws InvalidActionException {

        User user = getUserByConnectionId(connectionId);
        auth.authenticate(userName, password);
        User subscriber = getSubscriberByUserName(userName);
        subscriber.makeCart(user);
        connections.put(connectionId, subscriber);
    }

    public void logout(String connectionId) throws InvalidActionException {

        Subscriber subscriber = getUserByConnectionId(connectionId).getSubscriber();
        User guest = new User();
        guest.makeCart(subscriber);
        connections.put(connectionId, guest);
    }

    public int newStore(Subscriber subscriber, String storeName) throws InvalidActionException {

        int id = storeIdCounter.getAndIncrement();

        // create the new store
        Store store = new Store(id, storeName, "description", new defaultPurchasePolicy());
        stores.put(id, store);

        subscriber.addOwnerPermission(store);

        return id;
    }

    public Collection<Subscriber> getStoreStaff(Subscriber subscriber, Store store, Collection<Subscriber> staff) throws InvalidActionException {

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

    public void purchaseCart(User user) throws InvalidActionException {

        user.purchaseCart(paymentSystem, deliverySystem);
    }
}
