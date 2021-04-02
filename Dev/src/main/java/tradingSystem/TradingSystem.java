package tradingSystem;

import authentication.*;
import exceptions.*;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import store.Store;
import user.Subscriber;
import user.User;

import java.util.*;

public class TradingSystem {

    private final DeliverySystem deliverySystem;
    private final PaymentSystem paymentSystem;
    private final UserAuthentication auth;
    private final Map<String, Subscriber> subscribers; // key: user name
    private final Map<String, User> connections; // key: connection id
    private final Map<String, Store> stores; // key: store id

    public TradingSystem(String userName, String password, PaymentSystem paymentSystem, DeliverySystem deliverySystem,
                         UserAuthentication auth, Map<String, Subscriber> subscribers, Map<String, User> connections,
                         Map<String, Store> stores) throws LoginException {
        this.paymentSystem = paymentSystem;
        this.deliverySystem = deliverySystem;
        this.auth = auth;
        this.subscribers = subscribers;
        this.connections = connections;
        this.stores = stores;
        try {
            auth.authenticate(userName, password); // TODO check if the userName is admin
        } catch (SubscriberDoesNotExistException | WrongPasswordException e) {
            throw new LoginException(e);
        }
    }

    public User getUserByConnectionId(String connectionId) throws ConnectionIdDoesNotExistException {
        User user = connections.get(connectionId);
        if (user == null)
            throw new ConnectionIdDoesNotExistException();
        return user;
    }

    public Subscriber getSubscriberByUserName(String userName) throws SubscriberDoesNotExistException {
        Subscriber subscriber = subscribers.get(userName);
        if (subscriber == null)
            throw new SubscriberDoesNotExistException();
        return subscriber;
    }

    public Subscriber getSubscriberByConnectionId(String connectionId) throws ConnectionIdDoesNotExistException, SubscriberDoesNotExistException {
        Subscriber subscriber = getUserByConnectionId(connectionId).getSubscriber();
        if (subscriber == null)
            throw new SubscriberDoesNotExistException();
        return subscriber;
    }

    public Store getStore(String storeId) {
        return stores.get(storeId);
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
        } catch (ConnectionIdDoesNotExistException | SubscriberDoesNotExistException | WrongPasswordException e) {
            throw new LoginException(e);
        }
    }

    public void logout(String connectionId) throws ConnectionIdDoesNotExistException {
        User user = getUserByConnectionId(connectionId);
        User guest = new User(new HashMap<>());
        guest.makeCart(user);
        connections.put(connectionId, guest);
    }
}
