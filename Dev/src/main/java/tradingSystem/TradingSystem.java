package tradingSystem;

import authentication.UserAuthentication;
import exceptions.*;
import externalServices.DeliveryData;
import externalServices.DeliverySystem;
import externalServices.PaymentData;
import externalServices.PaymentSystem;
import purchaseAndReview.Purchase;
import purchaseAndReview.Review;
import store.Item;
import store.Store;
import user.*;

import java.util.*;

public class TradingSystem {

    private int storeIdCounter = 1;
    private int purchaseIdCounter = 1;
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

    public void purchaseCart(String connectionId) throws ConnectionIdDoesNotExistException, ExternalServicesException, Exception {
        User user = getUserByConnectionId(connectionId);
        double paymentValue = 0, storePayment = 0;
        Collection<Integer> itemIDs = new LinkedList<>();
        String details = "";
        String storeDetails = "";
        Map<Integer, Collection<Integer>> storeItems = new HashMap<>();
        Map<Store, Double> storePayments = new HashMap<>();
        for (Map.Entry<Store, Basket> entry : user.getCart().entrySet()) {
            storePayment = entry.getKey().calculate(entry.getValue().getItems());
            storePayments.put(entry.getKey(), storePayment);
            paymentValue += storePayment;
        }
        PaymentData paymentData = new PaymentData(paymentValue);
        if (paymentSystem.pay(paymentData)) {
            if (deliverySystem.deliver(new DeliveryData())) {
                for (Map.Entry<Store, Basket> entry : user.getCart().entrySet()) {
                    entry.getKey().unlockItems(entry.getValue().getItems().keySet());
                }
                for (Map.Entry<Store, Basket> entry : user.getCart().entrySet()) {
                    storeDetails = "Store Name: " + entry.getKey().getName() + "\nItems:\n";
                    for (Item item : entry.getValue().getItems().keySet()) {
                        itemIDs.add(item.getId());
                        storeDetails += "\titem name: " + item.getName() + " price: " + item.getPrice() + " quantity: " + entry.getValue().getItems().get(item) + "\n";
                    }
                    storeDetails += "Store Price: " + storePayments.get(entry.getKey()) + "\n";
                    details += storeDetails + "\n";
                    storeItems.put(entry.getKey().getId(), itemIDs);
                    Map<Integer, Collection<Integer>> itemsOfStore = new HashMap<>();
                    itemsOfStore.put(entry.getKey().getId(), itemIDs);
                    entry.getKey().addPurchase(new Purchase(purchaseIdCounter, itemsOfStore, storeDetails));
                    itemIDs = new LinkedList<>();
                }
                details += "Total Price: " + paymentValue;
                Purchase purchase = new Purchase(purchaseIdCounter, storeItems, details);
                user.addPurchase(purchase);
                purchaseIdCounter++;
                user.resetCart();
            } else {
                paymentSystem.payBack(paymentData);
                for (Map.Entry<Store, Basket> entry : user.getCart().entrySet()) {
                    entry.getKey().rollBack(entry.getValue().getItems());
                }
                throw new DeliverySystemException();
            }
        } else {
            for (Map.Entry<Store, Basket> entry : user.getCart().entrySet()) {
                entry.getKey().rollBack(entry.getValue().getItems());
            }
            throw new PaymentSystemException();
        }
    }

    public void writeOpinionOnProduct(String connectionId, String storeID, String productId, String desc) throws ConnectionIdDoesNotExistException, ItemException, NotLoggedInException {
        Subscriber subscriber = getSubscriberByConnectionId(connectionId);
//        User user = getUserByConnectionId(connectionId);
        for (Purchase purchase : subscriber.getPurchases()) {
            if(purchase.getStoreItems().containsKey(Integer.parseInt(storeID)))
                if(purchase.getStoreItems().get(Integer.parseInt(storeID)).contains(Integer.parseInt(productId)))
                {
                    Store store = stores.get(Integer.parseInt(storeID));
                    Item item = store.searchItemById(Integer.parseInt(productId));
                    item.addReview(new Review(subscriber, store, item, desc));
                    return;
                }
        }
        throw new ItemNotPurchased("can't write a review for an item that have not purchased by the user");
    }
}
