package tradingSystem;

import authentication.UserAuthentication;
import exceptions.*;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import store.Item;
import store.Store;
import user.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

public class TradingSystem {

    private int storeIdCounter = 0;
    private int purchaseIdCounter = 0;
    private int itemIdCounter = 0;

    private final DeliverySystem deliverySystem;
    private final PaymentSystem paymentSystem;
    private final UserAuthentication auth;

    private final Map<String, Subscriber> subscribers; // key: user name
    private final Map<String, User> connections; // key: connection id
    private final Map<Integer, Store> stores; // key: store id

    TradingSystem(String userName, String password, PaymentSystem paymentSystem, DeliverySystem deliverySystem,
                  UserAuthentication auth, Map<String, Subscriber> subscribers, Map<String, User> connections, Map<Integer, Store> stores)
            throws SubscriberDoesNotExistException, WrongPasswordException {

        this.paymentSystem = paymentSystem;
        this.deliverySystem = deliverySystem;
        this.auth = auth;
        this.subscribers = subscribers;
        this.connections = connections;
        this.stores = stores;

        auth.authenticate(userName, password); // TODO check if the userName is admin
    }

    public User getUserByConnectionId(String connectionId) throws InvalidConnectionIdException {
        User user = connections.get(connectionId);
        if (user == null)
            throw new InvalidConnectionIdException(connectionId);
        return user;
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

    public Store getStore(int storeId) throws InvalidStoreIdException {
        Store store = stores.get(storeId);
        if (store == null)
            throw new InvalidStoreIdException(storeId);
        return store;
    }

    public void register(String userName, String password) throws SubscriberAlreadyExistsException {
        auth.register(userName, password);
        subscribers.put(userName, new Subscriber(userName));
    }

    public String connect()
    {
        String connectionId = java.util.UUID.randomUUID().toString();
        // if need to be sticklers about uniqueness switch to org.springframework.util.AlternativeJdkIdGenerator

        connections.put(connectionId, new User());
        return connectionId;
    }

    public void login(String connectionId, String userName, String password)
            throws InvalidConnectionIdException, SubscriberDoesNotExistException, WrongPasswordException {

        User user = getUserByConnectionId(connectionId);
        auth.authenticate(userName, password);
        User subscriber = getSubscriberByUserName(userName);
        subscriber.makeCart(user);
        connections.put(connectionId, subscriber);
    }

    public void logout(String connectionId) throws InvalidConnectionIdException, NotLoggedInException {
        Subscriber subscriber = getUserByConnectionId(connectionId).getSubscriber();
        User guest = new User();
        guest.makeCart(subscriber);
        connections.put(connectionId, guest);
    }

    public int newStore(Subscriber subscriber, String storeName) throws ItemException {

        // create the new store
        Store store = new Store(this, storeIdCounter, storeName, "description");
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

    public void purchaseCart(User user) throws Exception { // TODO exception
        user.purchaseCart(paymentSystem, deliverySystem);
    }

    public int getNextItemId() {
        return itemIdCounter++;
    }
}
