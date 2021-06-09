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

    @Test (threadPoolSize = 1000, invocationCount = 10000, timeOut = 200000)
    public void complexTest() throws InvalidActionException {
        synchronized (subscribersConnections) {
            int i = subscriberId.getAndIncrement();
            subscribersConnections.add(tradingSystemService.connect());
            tradingSystemService.register("s" + i, "1234");
            tradingSystemService.login(subscribersConnections.get(i), "s" + i, "1234");
            paymentSystem.setFake(true);
            deliverySystem.setFake(true);
            if (i % 10 == 0) {
                int store = storeId.getAndIncrement();
                tradingSystemService.openNewStore(subscribersConnections.get(i), "eBay" + i);
                for (int j = 0; j < 500; j++) {
                    tradingSystemService.addProductToStore(subscribersConnections.get(i), String.valueOf(store), "bamba" + j, "snacks", "sub1", 10000, 5.5);
                    tradingSystemService.addProductToStore(subscribersConnections.get(i), String.valueOf(store), "bisli" + j, "snacks", "sub1", 10000, 5.5);
                }
                try {
                    if (i == 0)
                        max = 1000;
                    for (int x = 0; x < max; x++) {
                        String conn = tradingSystemService.connect();
                        tradingSystemService.addItemToBasket(conn, String.valueOf(store), String.valueOf(x), 1);
                        tradingSystemService.purchaseCart(conn, "1", 1, 2022, "1", "1", "1", "1", "1", "1", "1", 1);
                    }
                    if (i != 0) {
                        for (int x = 0; x < max; x++) {
                            tradingSystemService.addItemToBasket(subscribersConnections.get(i - 5), String.valueOf(store), String.valueOf(x), 1);
                            tradingSystemService.purchaseCart(subscribersConnections.get(i - 5), "1", 1, 2022, "1", "1", "1", "1", "1", "1", "1", 1);
                        }
                    }
                } catch (PaymentSystemException | DeliverySystemException e) {
                    //nothing
                }
            }
        }
    }
}
