package loadAndStressTests;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
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

public class LoadTestRegisterLoginLogout {

    private TradingSystemServiceImpl tradingSystemService;
    private final String userName = "Admin";
    private final String password = "123";
    private final ConcurrentHashMap<String, Subscriber> subscribers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, User> connections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Store> stores = new ConcurrentHashMap<>();
    private final UserAuthentication auth = new UserAuthentication();
    private final Subscriber admin = new Subscriber(0, userName);
    private final AtomicInteger subscriberId = new AtomicInteger(0);
    private long start;

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
        start = System.nanoTime();
    }

    @Test(threadPoolSize = 10, invocationCount = 1000, timeOut = 1500)
    public void test() throws InvalidActionException {
        String conn = tradingSystemService.connect();
        int id = subscriberId.getAndIncrement();
        tradingSystemService.register("s" + id, "1234");
        tradingSystemService.login(conn, "s" + id, "1234");
        tradingSystemService.logout(conn);
    }

    @AfterClass
    public void tearDown() {
        System.out.println((System.nanoTime() - start) / 1000000);
        assertTrue((System.nanoTime() - start) / 1000000 < 1500);
    }
}
