package loadAndStressTests;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import externalServices.*;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.TradingSystemServiceImpl;
import store.Store;
import tradingSystem.TradingSystemBuilder;
import tradingSystem.TradingSystemImpl;
import user.AdminPermission;
import user.Subscriber;
import user.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SystemAliveWithoutExternalServicesTest {

    private TradingSystemServiceImpl tradingSystemService;
    private final String userName = "Admin";
    private final String password = "123";
    private final ConcurrentHashMap<String, Subscriber> subscribers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, User> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Store> stores = new ConcurrentHashMap<>();
    private final UserAuthentication auth = new UserAuthentication();
    private final Subscriber admin = new Subscriber(0, userName);
    private PaymentSystemRealMock paymentSystem;
    private DeliverySystemRealMock deliverySystem;
    private final AtomicInteger index = new AtomicInteger(0);

    @BeforeClass
    void setUp() throws InvalidActionException {
        MockitoAnnotations.openMocks(this);
        auth.register(userName, password);
        admin.addPermission(AdminPermission.getInstance());
        subscribers.put(userName, admin);
        paymentSystem = new PaymentSystemRealMock();
        deliverySystem = new DeliverySystemRealMock();
        tradingSystemService = new TradingSystemServiceImpl(new TradingSystemImpl(new TradingSystemBuilder().setUserName(userName)
                .setPassword(password)
                .setSubscribers(subscribers)
                .setConnections(connections)
                .setStores(stores)
                .setAuth(auth)
                .setPaymentSystem(paymentSystem)
                .setDeliverySystem(deliverySystem).build()));

        String conn = tradingSystemService.connect();
        tradingSystemService.login(conn, userName, password);
        tradingSystemService.openNewStore(conn, "eBay");
        tradingSystemService.addProductToStore(conn, "0", "bamba", "snacks", "sub1", 2000, 5.5);
        paymentSystem.connect();
        deliverySystem.connect();
    }

    @Test (threadPoolSize = 100, invocationCount = 100, timeOut = 2000)
    public void test() throws InvalidActionException {
        String conn = tradingSystemService.connect();
        int id = index.getAndIncrement();
        tradingSystemService.register("s" + id, "1234");
        tradingSystemService.login(conn, "s" + id, "1234");
        tradingSystemService.addItemToBasket(conn,"0", "0", 1);
        if(id % 2 == 0) {
            paymentSystem.setFake(true);
            deliverySystem.setFake(false);
        }
        else {
            paymentSystem.setFake(false);
            deliverySystem.setFake(true);
        }
        tradingSystemService.purchaseCart(conn, "1", 1, 2022, "1", "1", "1", "1", "1", "1", "1", 1);
        tradingSystemService.openNewStore(conn, "eBay" + id);
    }
}
