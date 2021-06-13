package loadAndStressTests;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import externalServices.DeliverySystemRealMock;
import externalServices.PaymentSystemRealMock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterClass;
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

import static org.testng.AssertJUnit.assertTrue;

public class ConnectAndPurchaseBySubscribers {

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
    private long start, end;
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
        for(int i = 0; i < 1000; i++) {
            tradingSystemService.register("s" + i, "1234");
        }
        start = System.nanoTime();
    }

    @Test (threadPoolSize = 100, invocationCount = 1000, timeOut = 12000)
    public void test() throws InvalidActionException {
        String conn = tradingSystemService.connect();
        int id = index.getAndIncrement();
        tradingSystemService.login(conn, "s" + id, "1234");
        tradingSystemService.addItemToBasket(conn,"0", "0", 1);
        tradingSystemService.purchaseCart(conn, "1", 1, 2022, "1", "1", "1", "1", "1", "1", "1", 1);
    }

    @AfterClass
    public void tearDown() {
        System.out.println(paymentSystem.getTime());
        System.out.println(deliverySystem.getTime());
        end = System.nanoTime();
        System.out.println((end - start) / 1000000);
        assertTrue((System.nanoTime() - start) / 1000000 < 12000);
    }
}
