package tradingSystem;

import authentication.UserAuthentication;
import exceptions.*;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import policies.*;
import store.Item;
import store.Store;
import user.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TradingSystem {

    private final AtomicInteger storeIdCounter = new AtomicInteger();
    private final AtomicInteger subscriberIdCounter;
    private final AtomicInteger policyIdCounter = new AtomicInteger();
    private final AtomicInteger discountIdCounter = new AtomicInteger();

    private final DeliverySystem deliverySystem;
    private final PaymentSystem paymentSystem;
    private final UserAuthentication auth;

    private final ConcurrentHashMap<String, Subscriber> subscribers; // key: user name
    private final ConcurrentHashMap<Integer, Store> stores; // key: store id
    private final ConcurrentHashMap<String, User> connections; // key: connection id
    private final ConcurrentHashMap<Integer, PurchasePolicy> purchasePolicies; // key: purchase policy id
    private final ConcurrentHashMap<Integer, DiscountPolicy> discountPolicies; // key: discount policy id
    private final ConcurrentHashMap<Store, Collection<Integer>> storesPurchasePolicies; // key: store, value: purchase policies
    private final ConcurrentHashMap<Store, Collection<Integer>> storesDiscountPolicies; // key: store, value: discount policies


    TradingSystem(String userName, String password, AtomicInteger subscriberIdCounter, PaymentSystem paymentSystem, DeliverySystem deliverySystem,
                  UserAuthentication auth, ConcurrentHashMap<String, Subscriber> subscribers, ConcurrentHashMap<String, User> connections,
                  ConcurrentHashMap<Integer, Store> stores, ConcurrentHashMap<Integer, PurchasePolicy> purchasePolicies,
                  ConcurrentHashMap<Integer, DiscountPolicy> discountPolicies, ConcurrentHashMap<Store, Collection<Integer>> storesPurchasePolicies,
                  ConcurrentHashMap<Store, Collection<Integer>> storesDiscountPolicies)
            throws InvalidActionException {

        this.subscriberIdCounter = subscriberIdCounter;
        this.paymentSystem = paymentSystem;
        this.deliverySystem = deliverySystem;
        this.auth = auth;
        this.subscribers = subscribers;
        this.connections = connections;
        this.stores = stores;
        this.purchasePolicies = purchasePolicies;
        this.discountPolicies = discountPolicies;
        this.storesPurchasePolicies = storesPurchasePolicies;
        this.storesDiscountPolicies = storesDiscountPolicies;

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

        Store store = new Store(id, storeName, "description", null, null);

        for (Store s: stores.values()) {
            if(storeName.equals(s.getName()))
                throw new StoreAlreadyExistsException();
        }

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

    public Collection<Integer> getStorePolicies(Store store) {
        Collection<Integer> storePolicies = new LinkedList<>();
        for (Integer purchasePolicy: storesPurchasePolicies.get(store)) {
            storePolicies.add(purchasePolicy);
        }
        return storePolicies;
    }

    public void assignPolicy(int policy, Store store) {
        store.setPurchasePolicy(purchasePolicies.get(policy));
    }

    public void removePolicy(Store store, int policy) { //TODO add exception
        purchasePolicies.remove(policy);
        PurchasePolicy purchasePolicy = new DefaultPurchasePolicy();
        store.setPurchasePolicy(purchasePolicy);
    }

    public int makeQuantityPolicy(Store store, Collection<Item> items, int minQuantity, int maxQuantity) throws PolicyException {
        int id = policyIdCounter.getAndIncrement();
        PurchasePolicy purchasePolicy = new QuantityPolicy(items, minQuantity, maxQuantity);
        purchasePolicies.put(id, purchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        store.setPurchasePolicy(purchasePolicy);
        return id;
    }

    public int makeBasketPurchasePolicy(Store store, int minBasketValue) throws PolicyException{
        int id = policyIdCounter.getAndIncrement();
        PurchasePolicy purchasePolicy = new BasketPurchasePolicy(minBasketValue);
        purchasePolicies.put(id, purchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        store.setPurchasePolicy(purchasePolicy);
        return id;
    }

    public int makeTimePolicy(Store store, Collection<Item> items, LocalTime time) {
        int id = policyIdCounter.getAndIncrement();
        PurchasePolicy purchasePolicy = new TimePolicy(items, time);
        purchasePolicies.put(id, purchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        store.setPurchasePolicy(purchasePolicy);
        return id;
    }

    public int andPolicy(Store store, int policy1, int policy2) {

        int id = policyIdCounter.getAndIncrement();
        Collection<PurchasePolicy> andPolicies = new ArrayList<>();
        andPolicies.add(purchasePolicies.get(policy1));
        andPolicies.add(purchasePolicies.get(policy2));
        PurchasePolicy andPurchasePolicy = new AndPolicy(andPolicies);
        purchasePolicies.put(id, andPurchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        store.setPurchasePolicy(andPurchasePolicy);
        return id;
    }

    public int orPolicy(Store store, int policy1, int policy2) {

        int id = policyIdCounter.getAndIncrement();
        Collection<PurchasePolicy> orPolicies = new ArrayList<>();
        orPolicies.add(purchasePolicies.get(policy1));
        orPolicies.add(purchasePolicies.get(policy2));
        PurchasePolicy orPurchasePolicy = new OrPolicy(orPolicies);
        purchasePolicies.put(id, orPurchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        store.setPurchasePolicy(orPurchasePolicy);
        return id;
    }

    public int xorPolicy(Store store, int policy1, int policy2) {

        int id = policyIdCounter.getAndIncrement();
        Collection<PurchasePolicy> xorPolicies = new ArrayList<>();
        xorPolicies.add(purchasePolicies.get(policy1));
        xorPolicies.add(purchasePolicies.get(policy2));
        PurchasePolicy xorPurchasePolicy = new XorPolicy(xorPolicies);
        purchasePolicies.put(id, xorPurchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        store.setPurchasePolicy(xorPurchasePolicy);
        return id;
    }

    public void purchaseCart(User user) throws InvalidActionException {

        user.purchaseCart(paymentSystem, deliverySystem);
    }
}
