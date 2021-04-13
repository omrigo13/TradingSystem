package acceptanceTests;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import exceptions.NoPermissionException;
import exceptions.SubscriberDoesNotExistException;
import exceptions.WrongPasswordException;
import service.TradingSystemService;
import service.TradingSystemServiceImpl;
import tradingSystem.TradingSystem;
import tradingSystem.TradingSystemBuilder;
import user.AdminPermission;
import user.Subscriber;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put(userName, password);
        UserAuthentication userAuthentication = new UserAuthentication(map);
        ConcurrentHashMap<String, Subscriber> subscribers = new ConcurrentHashMap<>();
        Subscriber admin = new Subscriber(userName);
        admin.addPermission(AdminPermission.getInstance());
        subscribers.put(userName, admin);
        TradingSystem build = new TradingSystemBuilder().setUserName(userName).setPassword(password)
                .setSubscribers(subscribers).setAuth(userAuthentication).build();
        map.clear();
        TradingSystemServiceImpl real = new TradingSystemServiceImpl(build);
        proxy.setReal(real);
        return proxy;
    }

}
