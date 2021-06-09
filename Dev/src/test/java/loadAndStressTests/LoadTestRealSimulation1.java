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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadTestRealSimulation1 {

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

    @Test(threadPoolSize = 10, invocationCount = 100, timeOut = 10000)
    public void test() throws InvalidActionException {
        String conn = tradingSystemService.connect();
        int id = subscriberId.getAndIncrement();
        if(id < 70)
            tradingSystemService.login(conn, "s" + id, "1234");
        if(id < 10) {
            tradingSystemService.appointStoreOwner(conn, "s" + (id + 40), String.valueOf(id));
        }
        else if(id < 30) {
            tradingSystemService.addProductToStore(conn, String.valueOf(id), "bamba" + id, "snacks", "sub1", 500, 5.5);
        }
        else if(id < 50) {
            try { tradingSystemService.addItemToBasket(conn, String.valueOf(id - 30), "1", 5); }
            catch (ItemNotFoundException e){ tradingSystemService.addItemToBasket(conn, String.valueOf(id - 30), "0", 5); }
            tradingSystemService.purchaseCart(conn, "1", 1, 2022, "1", "1", "1", "1", "1", "1", "1", 1);
            try {tradingSystemService.writeOpinionOnProduct(conn, String.valueOf(id - 30), "0", "description"); }
            catch (ItemNotPurchasedException e) {tradingSystemService.writeOpinionOnProduct(conn, String.valueOf(id - 30), "1", "description"); }
            catch (NullPointerException e) {
                // nothing
            }
        }
    }

    @AfterClass
    public void tearDown() {
        end = System.nanoTime();
        System.out.println((end - start) / 1000000);
    }
}
