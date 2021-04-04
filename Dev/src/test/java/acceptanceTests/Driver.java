package acceptanceTests;

import service.TradingSystemService;

public class Driver {

    public static TradingSystemService getService(){
        ServiceProxy proxy = new ServiceProxy();
        // uncomment when real application is ready
        // proxy.setReal(new TradingSystemService());
        return proxy;
    }

}
