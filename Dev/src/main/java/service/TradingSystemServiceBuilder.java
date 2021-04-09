package service;

import authentication.UserAuthentication;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import store.Store;
import user.Subscriber;
import user.User;

import java.util.Map;

public class TradingSystemServiceBuilder {

    private UserAuthentication userAuthentication;
    private PaymentSystem paymentSystem;
    private DeliverySystem deliverySystem;
    private Map<String, Subscriber> subscribers;
    private Map<String, User> connections;
    private Map<Integer, Store> stores;

    public TradingSystemServiceBuilder setUserAuthentication(UserAuthentication userAuthentication) {
        this.userAuthentication = userAuthentication;
        return this;
    }

    public TradingSystemServiceBuilder setPaymentSystem(PaymentSystem paymentSystem) {
        this.paymentSystem = paymentSystem;
        return this;
    }

    public TradingSystemServiceBuilder setDeliverySystem(DeliverySystem deliverySystem) {
        this.deliverySystem = deliverySystem;
        return this;
    }

    public TradingSystemServiceBuilder setSubscribers(Map<String, Subscriber> subscribers) {
        this.subscribers = subscribers;
        return this;
    }

    public TradingSystemServiceBuilder setConnections(Map<String, User> connections) {
        this.connections = connections;
        return this;
    }

    public TradingSystemServiceBuilder setStores(Map<Integer, Store> stores) {
        this.stores = stores;
        return this;
    }

    public TradingSystemServiceImpl create() {
        return new TradingSystemServiceImpl(userAuthentication, paymentSystem, deliverySystem, subscribers, connections, stores);
    }
}