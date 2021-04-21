package acceptanceTests;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import service.TradingSystemService;
import service.TradingSystemServiceImpl;
import tradingSystem.TradingSystemImpl;
import tradingSystem.TradingSystem;
import tradingSystem.TradingSystemBuilder;
import user.AdminPermission;
import user.Subscriber;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Driver {

    /**
     *
     * @param userName - system manager username to register in UserAuthenticator
     * @param password - pass for system manager
     * @return
     */
    public static TradingSystemService getService(String userName, String password) throws InvalidActionException {
        ServiceProxy proxy = new ServiceProxy();
        // uncomment when real application is ready
        UserAuthentication auth = new UserAuthentication();
        auth.register(userName, password);
        ConcurrentHashMap<String, Subscriber> subscribers = new ConcurrentHashMap<>();
        AtomicInteger subscriberIdCounter = new AtomicInteger();
        Subscriber admin = new Subscriber(subscriberIdCounter.getAndIncrement(), userName);
        admin.addPermission(AdminPermission.getInstance());
        subscribers.put(userName, admin);
        TradingSystem build = new TradingSystemBuilder().setUserName(userName).setPassword(password)
                .setSubscriberIdCounter(subscriberIdCounter).setSubscribers(subscribers).setAuth(auth).build();
        TradingSystemImpl trade = new TradingSystemImpl(build);
        TradingSystemServiceImpl real = new TradingSystemServiceImpl(trade);
        proxy.setReal(real);
        return proxy;
    }

}
