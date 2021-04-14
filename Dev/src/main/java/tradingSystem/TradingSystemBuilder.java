package tradingSystem;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import store.Store;
import user.Subscriber;
import user.User;

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

    public TradingSystemBuilder setSubscriberIdCounter(AtomicInteger subscriberIdCounter){

        this.subscriberIdCounter = subscriberIdCounter;
        return this;
    }

    public TradingSystem build() throws InvalidActionException {

        paymentSystem = (paymentSystem == null) ? new PaymentSystem() : paymentSystem;
        deliverySystem = (deliverySystem == null) ? new DeliverySystem() : deliverySystem;
        auth = (auth == null) ? new UserAuthentication(new ConcurrentHashMap<>()) : auth;
        subscribers = (subscribers == null) ? new ConcurrentHashMap<>() : subscribers;
        connections = (connections == null) ? new ConcurrentHashMap<>() : connections;
        stores = (stores == null) ? new ConcurrentHashMap<>() : stores;
        subscriberIdCounter = (subscriberIdCounter == null) ? new AtomicInteger() : subscriberIdCounter;

        return new TradingSystem(userName, password, subscriberIdCounter, paymentSystem, deliverySystem, auth, subscribers, connections, stores);
    }
}