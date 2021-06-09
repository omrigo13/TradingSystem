package loadAndStressTests;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import exceptions.ItemNotFoundException;
import exceptions.ItemNotPurchasedException;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadTestRealSimulation2 {

    private TradingSystemServiceImpl tradingSystemService;
    private final String userName = "Admin";
    private final String password = "123";
    private final ConcurrentHashMap<String, Subscriber> subscribers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, User> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Store> stores = new ConcurrentHashMap<>();
    private final UserAuthentication auth = new UserAuthentication();
    private final Subscriber admin = new Subscriber(0, userName);
    private final AtomicInteger subscriberId = new AtomicInteger(0);
    private long start, end;

    @BeforeClass
    void setUp() throws InvalidActionException {
        MockitoAnnotations.openMocks(this);
        MockitoAnnotations.openMocks(this);
        auth.register(userName, password);
        admin.addPermission(AdminPermission.getInstance());
        subscribers.put(userName, admin);
        tradingSystemService = new TradingSystemServiceImpl(new TradingSystemImpl(new TradingSystemBuilder().setUserName(userName)
                .setPassword(password)
                .setSubscribers(subscribers)
                .setConnections(connections)
                .setStores(stores)
                .setAuth(auth)
                .build()));
        String conn;
        for(int i = 0; i < 70; i++){
            conn = tradingSystemService.connect();
            tradingSystemService.register("s" + i, "1234");
            tradingSystemService.login(conn, "s" + i, "1234");
        }
        for(int i = 0; i < 30; i++){
            conn = tradingSystemService.connect();
            tradingSystemService.login(conn, "s" + i, "1234");
            String store = tradingSystemService.openNewStore(conn, "eBay" + i);
            tradingSystemService.addProductToStore(conn, store, "bisli", "snacks", "sub1", 500, 5.5);
        }
        start = System.nanoTime();
    }

    @Test(threadPoolSize = 10, invocationCount = 100, timeOut = 5000)
    public void test() throws InvalidActionException {
        String conn = tradingSystemService.connect();
        int id = subscriberId.getAndIncrement();
        if(id < 70)
            tradingSystemService.login(conn, "s" + id, "1234");
        if(id < 10) {
            tradingSystemService.appointStoreOwner(conn, "s" + (id + 40), String.valueOf(id));
            String conn2 = tradingSystemService.connect();
            tradingSystemService.login(conn2, "s" + (id + 40), "1234");
            tradingSystemService.appointStoreManager(conn2, "s" + (id + 60), String.valueOf(id));
            tradingSystemService.allowManagerToUpdateProducts(conn2, String.valueOf(id), "s" + (id + 60));
            String conn3 = tradingSystemService.connect();
            tradingSystemService.login(conn3, "s" + (id + 60), "1234");
            tradingSystemService.updateProductDetails(conn3, String.valueOf(id), "0", null, null, 3.5);
        }
        tradingSystemService.addItemToBasket(conn, "0", "0", 3);
    }

    @AfterClass
    public void tearDown() {
        end = System.nanoTime();
        System.out.println((end - start) / 1000000);
    }
}
