package loadAndStressTests;

import authentication.UserAuthentication;
import exceptions.DeliverySystemException;
import exceptions.ExternalServicesException;
import exceptions.InvalidActionException;
import exceptions.PaymentSystemException;
import externalServices.DeliverySystemRealMock;
import externalServices.PaymentSystemRealMock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import service.TradingSystemServiceImpl;
import store.Store;
import tradingSystem.TradingSystem;
import tradingSystem.TradingSystemBuilder;
import tradingSystem.TradingSystemImpl;
import user.AdminPermission;
import user.Subscriber;
import user.User;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.AssertJUnit.assertTrue;

public class LoadTestItemAndStoreAndPurchaseTest {

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
    private final AtomicInteger subscriberId = new AtomicInteger(0);
    private final AtomicInteger storeId = new AtomicInteger(0);
    private final LinkedList<String> subscribersConnections = new LinkedList<>();
    private String adminId;
    private int max = 500;

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

        adminId = tradingSystemService.connect();
        tradingSystemService.login(adminId, userName, password);
        paymentSystem.connect();
        deliverySystem.connect();
        start = System.nanoTime();
    }

    @Test (threadPoolSize = 8, invocationCount = 100, timeOut = 200000)
    public void complexTest() throws InvalidActionException {
        int i = subscriberId.getAndIncrement();
        String conn = tradingSystemService.connect();
        tradingSystemService.register("s" + i, "1234");
        tradingSystemService.login(conn, "s" + i, "1234");
        paymentSystem.setFake(true);
        deliverySystem.setFake(true);
        if (i % 10 == 0) {
            String store = tradingSystemService.openNewStore(conn, "eBay" + i);
            for (int j = 0; j < 500; j++) {
                tradingSystemService.addProductToStore(conn, store, "bamba" + j, "snacks", "sub1", 10000, 5.5);
                tradingSystemService.addProductToStore(conn, store, "bisli" + j, "snacks", "sub1", 10000, 5.5);
            }
                try {
                    String conn2 = tradingSystemService.connect();
                    if(i != 0)
                        tradingSystemService.login(conn2, "s" + (i - 5), "1234");
                    if (i == 0)
                        max = 1000;
                    for (int x = 0; x < max; x++) {
                        tradingSystemService.addItemToBasket(conn2, store, String.valueOf(x), 1);
                        tradingSystemService.purchaseCart(conn2, "1", 1, 2022, "1", "1", "1", "1", "1", "1", "1", 1);
                    }
                    if (i != 0) {
                        for (int x = 0; x < max; x++) {
                            tradingSystemService.addItemToBasket(conn2, store, String.valueOf(x), 1);
                            tradingSystemService.purchaseCart(conn2, "1", 1, 2022, "1", "1", "1", "1", "1", "1", "1", 1);
                        }
                    }
                } catch (PaymentSystemException | DeliverySystemException e) {
                    //nothing
                }
            }
    }
}
