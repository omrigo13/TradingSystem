package tradingSystem;

import authentication.UserAuthentication;
import exceptions.*;
import externalServices.DeliveryData;
import externalServices.DeliverySystem;
import externalServices.PaymentData;
import externalServices.PaymentSystem;
import notifications.Observable;
import notifications.VisitorsNotification;
import policies.*;
import store.Item;
import store.Store;
import user.*;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
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
    private final ConcurrentHashMap<String, Map<String, Integer>> visitors; // key: date, value: Map of visitors per type
    private final Subscriber admin;

    //private final Map<Store, Observable> observables;

    TradingSystem(String userName, String password, AtomicInteger subscriberIdCounter, PaymentSystem paymentSystem, DeliverySystem deliverySystem,
                  UserAuthentication auth, ConcurrentHashMap<String, Subscriber> subscribers, ConcurrentHashMap<String, User> connections,
                  ConcurrentHashMap<Integer, Store> stores, ConcurrentHashMap<Integer, PurchasePolicy> purchasePolicies,
                  ConcurrentHashMap<Integer, DiscountPolicy> discountPolicies, ConcurrentHashMap<Store, Collection<Integer>> storesPurchasePolicies,
                  ConcurrentHashMap<Store, Collection<Integer>> storesDiscountPolicies, ConcurrentHashMap<String, Map<String, Integer>> visitors)
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
        this.visitors = visitors;

        auth.authenticate(userName, password);
        subscribers.get(userName).validatePermission(AdminPermission.getInstance());

        this.admin = subscribers.get(userName).getSubscriber();
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
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        visitors.putIfAbsent(date, new HashMap<>());
        visitors.get(date).putIfAbsent("guests", 0);
        visitors.get(date).putIfAbsent("system admins", 0);
        visitors.get(date).putIfAbsent("store owners", 0);
        visitors.get(date).putIfAbsent("store managers", 0);
        visitors.get(date).putIfAbsent("subscribers", 0);
        //noinspection ConstantConditions
        visitors.get(date).compute("guests", (k, v) -> v + 1);
        connections.put(connectionId, new User());
        admin.notifyVisitors(new VisitorsNotification(visitors.get(date)));
        return connectionId;
    }

    public void login(String connectionId, String userName, String password) throws InvalidActionException {

        User user = getUserByConnectionId(connectionId);
        auth.authenticate(userName, password);
        Subscriber subscriber = getSubscriberByUserName(userName);
        boolean managerAndOwner = false;
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        int managers = visitors.get(date).get("store managers"), owners = visitors.get(date).get("store owners");
        subscriber.makeCart(user);
        connections.put(connectionId, subscriber);
        subscriber.setLoggedIn(true);
        if(subscriber.havePermission(AdminPermission.getInstance())) {
            //noinspection ConstantConditions
            visitors.get(date).compute("system admins", (k, v) -> v + 1);
            admin.notifyVisitors(new VisitorsNotification(visitors.get(date)));
            return;
        }
        for (Store store : stores.values()) {
            if (subscriber.havePermission(OwnerPermission.getInstance(store))) {
                //noinspection ConstantConditions
                visitors.get(date).compute("store owners", (k, v) -> v + 1);
                if(managerAndOwner) {
                    //noinspection ConstantConditions
                    visitors.get(date).compute("store managers", (k, v) -> v - 1);
                }
                admin.notifyVisitors(new VisitorsNotification(visitors.get(date)));
                return;
            }
            if (subscriber.havePermission(ManagerPermission.getInstance(store))) {
                //noinspection ConstantConditions
                visitors.get(date).compute("store managers", (k, v) -> v + 1);
                managerAndOwner = true;
            }
        }
        if(managers == visitors.get(date).get("store managers") && owners == visitors.get(date).get("store owners")) {
            //noinspection ConstantConditions
            visitors.get(date).compute("subscribers", (k, v) -> v + 1);
        }
        admin.notifyVisitors(new VisitorsNotification(visitors.get(date)));
    }

    public void logout(String connectionId) throws InvalidActionException {

        getUserByConnectionId(connectionId).getSubscriber().setLoggedIn(false); // this is here in order to throw exceptions
        User guest = new User();
        connections.put(connectionId, guest);
    }

    synchronized public int newStore(Subscriber subscriber, String storeName) throws InvalidActionException {

        for (Store s : stores.values())
            if (storeName != null && storeName.equals(s.getName()))
                throw new StoreAlreadyExistsException();

        // create the new store
        int id = storeIdCounter.getAndIncrement();
        Store store = new Store(id, storeName, "description", null, null, new Observable());
        stores.put(id, store);

        subscriber.addOwnerPermission(store);

//        observables.put(store, new Observable());
        store.subscribe(subscriber);

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
                items.add("store: " + store.getId() + ", " + item.toString() + " quantity: " + store.getItems().get(item));
        }
        return items;
    }

    public Collection<Integer> getStorePolicies(Store store) {
        if (storesPurchasePolicies.get(store) == null)
            return new LinkedList<>();
        return new LinkedList<>(storesPurchasePolicies.get(store));
    }

    public void assignStorePurchasePolicy(int policy, Store store) throws PolicyException {
        if (!purchasePolicies.containsKey(policy))
            throw new PolicyException();
        store.setPurchasePolicy(purchasePolicies.get(policy));
    }

    public void removePolicy(Store store, int policy) throws PolicyException {
        if (!purchasePolicies.containsKey(policy))
            throw new PolicyException();
        if (purchasePolicies.get(policy).getPurchasePolicies().size() > 0)
            throw new RemovePurchasePolicyException();
        for (PurchasePolicy purchasePolicy : purchasePolicies.values()) {
            purchasePolicy.getPurchasePolicies().remove(purchasePolicies.get(policy));
        }
        purchasePolicies.remove(policy);
        storesPurchasePolicies.get(store).remove(policy);
    }

    public int makeQuantityPolicy(Store store, Collection<Item> items, int minQuantity, int maxQuantity) throws PolicyException {
        int id = policyIdCounter.getAndIncrement();
        PurchasePolicy purchasePolicy = new QuantityPolicy(items, minQuantity, maxQuantity);
        purchasePolicies.put(id, purchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        return id;
    }

    public int makeBasketPurchasePolicy(Store store, int minBasketValue) throws PolicyException {
        int id = policyIdCounter.getAndIncrement();
        PurchasePolicy purchasePolicy = new BasketPurchasePolicy(minBasketValue);
        purchasePolicies.put(id, purchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        return id;
    }

    public int makeTimePolicy(Store store, Collection<Item> items, LocalTime time) {
        int id = policyIdCounter.getAndIncrement();
        PurchasePolicy purchasePolicy = new TimePolicy(items, time);
        purchasePolicies.put(id, purchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        return id;
    }

    public int andPolicy(Store store, int policy1, int policy2) throws PolicyException {

        int id = policyIdCounter.getAndIncrement();
        Collection<PurchasePolicy> andPolicies = new ArrayList<>();
        if (!purchasePolicies.containsKey(policy1) || !purchasePolicies.containsKey(policy2))
            throw new PolicyException();
        andPolicies.add(purchasePolicies.get(policy1));
        andPolicies.add(purchasePolicies.get(policy2));
        PurchasePolicy andPurchasePolicy = new AndPolicy(andPolicies);
        purchasePolicies.put(id, andPurchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        return id;
    }

    public int orPolicy(Store store, int policy1, int policy2) throws PolicyException {

        int id = policyIdCounter.getAndIncrement();
        Collection<PurchasePolicy> orPolicies = new ArrayList<>();
        if (!purchasePolicies.containsKey(policy1) || !purchasePolicies.containsKey(policy2))
            throw new PolicyException();
        orPolicies.add(purchasePolicies.get(policy1));
        orPolicies.add(purchasePolicies.get(policy2));
        PurchasePolicy orPurchasePolicy = new OrPolicy(orPolicies);
        purchasePolicies.put(id, orPurchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        return id;
    }

    public int xorPolicy(Store store, int policy1, int policy2) throws PolicyException {

        int id = policyIdCounter.getAndIncrement();
        Collection<PurchasePolicy> xorPolicies = new ArrayList<>();
        if (!purchasePolicies.containsKey(policy1) || !purchasePolicies.containsKey(policy2))
            throw new PolicyException();
        xorPolicies.add(purchasePolicies.get(policy1));
        xorPolicies.add(purchasePolicies.get(policy2));
        PurchasePolicy xorPurchasePolicy = new XorPolicy(xorPolicies);
        purchasePolicies.put(id, xorPurchasePolicy);
        storesPurchasePolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesPurchasePolicies.get(store).add(id);
        return id;
    }

    public Collection<Integer> getStoreDiscounts(Store store) {
        if (storesDiscountPolicies.get(store) == null)
            return new LinkedList<>();
        return new LinkedList<>(storesDiscountPolicies.get(store));
    }

    public void assignStoreDiscountPolicy(int discountId, Store store) throws PolicyException {
        if (!discountPolicies.containsKey(discountId))
            throw new PolicyException();
        store.setDiscountPolicy(discountPolicies.get(discountId));
    }

    public void removeDiscount(Store store, int discountId) throws PolicyException {
        if (!discountPolicies.containsKey(discountId))
            throw new PolicyException();
        if (discountPolicies.get(discountId).getDiscountPolicies().size() > 0)
            throw new RemoveDiscountPolicyException();
        for (DiscountPolicy discountPolicy : discountPolicies.values()) {
            discountPolicy.getDiscountPolicies().remove(discountPolicies.get(discountId));
        }
        discountPolicies.remove(discountId);
        storesDiscountPolicies.get(store).remove(discountId);
    }

    public int makeQuantityDiscount(Store store, int discount, Collection<Item> items, Integer policyId) throws PolicyException {

        int id = discountIdCounter.getAndIncrement();
        PurchasePolicy purchasePolicy = null;
        if (policyId != null && !purchasePolicies.containsKey(policyId))
            throw new PolicyException();
        if (policyId != null)
            purchasePolicy = purchasePolicies.get(policyId);
        discountPolicies.put(id, new QuantityDiscountPolicy(discount, items, purchasePolicy));
        storesDiscountPolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesDiscountPolicies.get(store).add(id);
        return id;
    }

    public int makePlusDiscount(Store store, int discountId1, int discountId2) throws PolicyException {

        int id = discountIdCounter.getAndIncrement();
        Collection<DiscountPolicy> plusDiscountPolicies = new ArrayList<>();
        if (!discountPolicies.containsKey(discountId1) || !discountPolicies.containsKey(discountId2))
            throw new PolicyException();
        plusDiscountPolicies.add(discountPolicies.get(discountId1));
        plusDiscountPolicies.add(discountPolicies.get(discountId2));
        discountPolicies.put(id, new PlusDiscountPolicy(plusDiscountPolicies));
        storesDiscountPolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesDiscountPolicies.get(store).add(id);
        return id;
    }

    public int makeMaxDiscount(Store store, int discountId1, int discountId2) throws PolicyException {

        int id = discountIdCounter.getAndIncrement();
        Collection<DiscountPolicy> maxDiscountPolicies = new ArrayList<>();
        if (!discountPolicies.containsKey(discountId1) || !discountPolicies.containsKey(discountId2))
            throw new PolicyException();
        maxDiscountPolicies.add(discountPolicies.get(discountId1));
        maxDiscountPolicies.add(discountPolicies.get(discountId2));
        discountPolicies.put(id, new MaxDiscountPolicy(maxDiscountPolicies));
        storesDiscountPolicies.computeIfAbsent(store, k -> new ArrayList<>());
        storesDiscountPolicies.get(store).add(id);
        return id;
    }

    public void purchaseCart(User user, PaymentData paymentData, DeliveryData deliveryData) throws InvalidActionException {

        user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData);
    }

    public Collection<String> getTotalIncomeByAdminPerDay(Subscriber admin, String date) throws NoPermissionException {

        admin.validatePermission(AdminPermission.getInstance());

        Collection<String> storesValuesPerDay = new LinkedList<>();
        for (Store store: stores.values()) {
            storesValuesPerDay.add(admin.getTotalIncomeByStorePerDay(store, date));
        }
        return storesValuesPerDay;
    }

    public Map<String, Integer> getTotalVisitorsByAdminPerDay(Subscriber admin, String date) throws NoPermissionException {

        admin.validatePermission(AdminPermission.getInstance());

        return visitors.get(date);
    }
}
