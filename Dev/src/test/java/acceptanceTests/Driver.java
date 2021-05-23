package acceptanceTests;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
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

    private static PaymentSystem paymentSystem = new PaymentSystemMock();
    private static DeliverySystem deliverySystem = new DeliverySystemMock();

    public static PaymentSystem getPaymentSystem() {
        return paymentSystem;
    }

    public static DeliverySystem getDeliverySystem() {
        return deliverySystem;
    }

    public static void setPaymentSystem(PaymentSystem paymentSystem) {
        Driver.paymentSystem = paymentSystem;
    }

    public static void setDeliverySystem(DeliverySystem deliverySystem) {
        Driver.deliverySystem = deliverySystem;
    }

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
                .setSubscriberIdCounter(subscriberIdCounter).setSubscribers(subscribers).setAuth(auth).setPaymentSystem(paymentSystem).setDeliverySystem(deliverySystem).build();
        TradingSystemImpl trade = new TradingSystemImpl(build);
        TradingSystemServiceImpl real = new TradingSystemServiceImpl(trade);
        proxy.setReal(real);
        return proxy;
    }

}
