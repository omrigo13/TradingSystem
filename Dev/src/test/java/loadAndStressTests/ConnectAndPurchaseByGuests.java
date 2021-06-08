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

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectAndPurchaseByGuests {

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
    private final LinkedList<String> subscribersIds = new LinkedList<>();

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
        for(int i = 0; i < 100; i++) {
            subscribersIds.add(tradingSystemService.connect());
            tradingSystemService.register("s" + i, "1234");
            tradingSystemService.login(subscribersIds.get(i), "s" + i, "1234");
            tradingSystemService.addItemToBasket(subscribersIds.get(i),"0", "0", 1);
        }
        paymentSystem.connect();
        deliverySystem.connect();
        start = System.nanoTime();
    }

    @Test (threadPoolSize = 10, invocationCount = 100, timeOut = 5000)
    public void test() throws InvalidActionException {
        tradingSystemService.purchaseCart(subscribersIds.get(index.getAndIncrement()), "1", 1, 2022, "1", "1", "1", "1", "1", "1", "1", 1);
    }

    @AfterClass
    public void tearDown() {
        end = System.nanoTime();
        System.out.println((end - start) / 1000000);
        System.out.println(paymentSystem.getTime());
        System.out.println(deliverySystem.getTime());
    }
}
