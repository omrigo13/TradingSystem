package acceptanceTests;

import authentication.UserAuthentication;
import exceptions.SubscriberDoesNotExistException;
import exceptions.WrongPasswordException;
import service.TradingSystemService;
import service.TradingSystemServiceImpl;
import tradingSystem.TradingSystem;
import tradingSystem.TradingSystemBuilder;

import java.util.HashMap;
import java.util.Map;

public class Driver {

    /**
     *
     * @param userName - system manager username to register in UserAuthenticator
     * @param password - pass for system manager
     * @return
     */
    public static TradingSystemService getService(String userName, String password) throws SubscriberDoesNotExistException, WrongPasswordException {
        ServiceProxy proxy = new ServiceProxy();
        // uncomment when real application is ready
        Map<String, String> map = new HashMap<>();
        map.put(userName, password);
        UserAuthentication userAuthentication = new UserAuthentication(map);
        proxy.setReal(new TradingSystemServiceImpl(new TradingSystemBuilder().setUserName(userName).setPassword(password)
                .setAuth(userAuthentication).build()));
        return proxy;
    }

}
