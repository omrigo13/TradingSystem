package tradingSystem;

import authentication.UserAuthentication;
import exceptions.SubscriberDoesNotExistException;
import exceptions.WrongPasswordException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import store.Store;
import user.Subscriber;
import user.User;

import java.util.HashMap;
import java.util.Map;

public class TradingSystemBuilder {

    private String userName;
    private String password;
    private PaymentSystem paymentSystem;
    private DeliverySystem deliverySystem;
    private UserAuthentication auth;
    private Map<String, Subscriber> subscribers;
    private Map<String, User> connections;
    private Map<Integer, Store> stores;

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

    public TradingSystemBuilder setSubscribers(Map<String, Subscriber> subscribers) {
        this.subscribers = subscribers;
        return this;
    }

    public TradingSystemBuilder setConnections(Map<String, User> connections) {
        this.connections = connections;
        return this;
    }

    public TradingSystemBuilder setStores(Map<Integer, Store> stores) {
        this.stores = stores;
        return this;
    }

    public TradingSystem build() throws SubscriberDoesNotExistException, WrongPasswordException {

        paymentSystem = (paymentSystem == null) ? new PaymentSystem() : paymentSystem;
        deliverySystem = (deliverySystem == null) ? new DeliverySystem() : deliverySystem;
        subscribers = (subscribers == null) ? new HashMap<>() : subscribers;
        connections = (connections == null) ? new HashMap<>() : connections;
        stores = (stores == null) ? new HashMap<>() : stores;

        return new TradingSystem(userName, password, paymentSystem, deliverySystem, auth, subscribers, connections, stores);
    }
}