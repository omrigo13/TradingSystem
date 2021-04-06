package acceptanceTests;

import authentication.UserAuthentication;
import exceptions.SubscriberAlreadyExistsException;
import service.TradingSystemService;
import service.TradingSystemServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class Driver {

    /**
     *
     * @param userName - system manager username to register in UserAuthenticator
     * @param password - pass for system manager
     * @return
     */
    public static TradingSystemService getService(String userName, String password) {
        ServiceProxy proxy = new ServiceProxy();
        // uncomment when real application is ready
        Map<String, String> map = new HashMap<>();
        map.put(userName, password);
        UserAuthentication userAuthentication = new UserAuthentication(map);
        proxy.setReal(new TradingSystemServiceImpl(userAuthentication));
        return proxy;
    }

}
