package tradingSystem;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import externalServices.DeliverySystem;
import externalServices.DeliverySystemBasicImpl;
import externalServices.PaymentSystem;
import externalServices.PaymentSystemBasicImpl;
import policies.DiscountPolicy;
import policies.PurchasePolicy;
import store.Store;
import user.Subscriber;
import user.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TradingSystemBuilder {

    private String userName;
    private String password;
    private PaymentSystem paymentSystem;
    private DeliverySystem deliverySystem;
    private UserAuthentication auth;
    private ConcurrentHashMap<String, Subscriber> subscribers;
    private ConcurrentHashMap<Integer, Store> stores;
    private ConcurrentHashMap<String, User> connections;
    private AtomicInteger subscriberIdCounter;
    private ConcurrentHashMap<Integer, PurchasePolicy> purchasePolicies;
    private ConcurrentHashMap<Integer, DiscountPolicy> discountPolicies;
    private ConcurrentHashMap<Store, Collection<Integer>> storesPurchasePolicies;
    private ConcurrentHashMap<Store, Collection<Integer>> storesDiscountPolicies;
    private ConcurrentHashMap<String, Map<String, Integer>> visitors;

    public TradingSystemBuilder setUserName(String userName) {

        this.userName = userName;
        return this;
    }

    public TradingSystemBuilder setPassword(String password) {

        this.password = password;
        return this;
    }

    public TradingSystemBuilder setPaymentSystem(PaymentSystem paymentSystem) {

        this.paymentSystem = paymentSystem;
        return this;
    }

    public TradingSystemBuilder setDeliverySystem(DeliverySystem deliverySystem) {

        this.deliverySystem = deliverySystem;
        return this;
    }

    public TradingSystemBuilder setAuth(UserAuthentication auth) {

        this.auth = auth;
        return this;
    }

    public TradingSystemBuilder setSubscribers(ConcurrentHashMap<String, Subscriber> subscribers) {

        this.subscribers = subscribers;
        return this;
    }

    public TradingSystemBuilder setConnections(ConcurrentHashMap<String, User> connections) {

        this.connections = connections;
        return this;
    }

    public TradingSystemBuilder setStores(ConcurrentHashMap<Integer, Store> stores) {

        this.stores = stores;
        return this;
    }

    public TradingSystemBuilder setPurchasePolicies(ConcurrentHashMap<Integer, PurchasePolicy> purchasePolicies) {

        this.purchasePolicies = purchasePolicies;
        return this;
    }

    public TradingSystemBuilder setDiscountPolicies(ConcurrentHashMap<Integer, DiscountPolicy> discountPolicies) {

        this.discountPolicies = discountPolicies;
        return this;
    }

    public TradingSystemBuilder setStoresPurchasePolicies(ConcurrentHashMap<Store, Collection<Integer>> storesPurchasePolicies) {

        this.storesPurchasePolicies = storesPurchasePolicies;
        return this;
    }

    public TradingSystemBuilder setStoresDiscountPolicies(ConcurrentHashMap<Store, Collection<Integer>> storesDiscountPolicies) {

        this.storesDiscountPolicies = storesDiscountPolicies;
        return this;
    }

    public TradingSystemBuilder setSubscriberIdCounter(AtomicInteger subscriberIdCounter){

        this.subscriberIdCounter = subscriberIdCounter;
        return this;
    }

    public TradingSystem build() throws InvalidActionException {

        paymentSystem = (paymentSystem == null) ? new PaymentSystemBasicImpl() : paymentSystem;
        deliverySystem = (deliverySystem == null) ? new DeliverySystemBasicImpl() : deliverySystem;
        auth = (auth == null) ? new UserAuthentication() : auth;
        subscribers = (subscribers == null) ? new ConcurrentHashMap<>() : subscribers;
        connections = (connections == null) ? new ConcurrentHashMap<>() : connections;
        stores = (stores == null) ? new ConcurrentHashMap<>() : stores;
        purchasePolicies = (purchasePolicies == null) ? new ConcurrentHashMap<>() : purchasePolicies;
        discountPolicies = (discountPolicies == null) ? new ConcurrentHashMap<>() : discountPolicies;
        storesPurchasePolicies = (storesPurchasePolicies == null) ? new ConcurrentHashMap<>(): storesPurchasePolicies;
        storesDiscountPolicies = (storesDiscountPolicies == null) ? new ConcurrentHashMap<>(): storesDiscountPolicies;
        subscriberIdCounter = (subscriberIdCounter == null) ? new AtomicInteger() : subscriberIdCounter;
        visitors = (visitors == null) ? new ConcurrentHashMap() : visitors;

        return new TradingSystem(userName, password, subscriberIdCounter, paymentSystem, deliverySystem, auth, subscribers, connections, stores, purchasePolicies,
                discountPolicies, storesPurchasePolicies, storesDiscountPolicies, visitors);
    }
}