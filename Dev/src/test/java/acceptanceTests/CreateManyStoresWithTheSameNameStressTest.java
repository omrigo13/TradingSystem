package acceptanceTests;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import exceptions.StoreAlreadyExistsException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import store.Store;
import tradingSystem.TradingSystem;
import tradingSystem.TradingSystemBuilder;
import user.Subscriber;
import user.User;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;

public class CreateManyStoresWithTheSameNameStressTest {

    private TradingSystem tradingSystem;

    @Mock private UserAuthentication auth;
    @Mock private ConcurrentHashMap<String, Subscriber> subscribers;
    @Mock private ConcurrentHashMap<String, User> connections;
    @Mock private Subscriber subscriber;

    private ConcurrentHashMap<Integer, Store> stores;
    private final AtomicInteger storeCreationCounter = new AtomicInteger();
    private final AtomicInteger storeCreationTrialNumber = new AtomicInteger();

    @BeforeClass
    void setUp() throws InvalidActionException {
        MockitoAnnotations.openMocks(this);
        stores = new ConcurrentHashMap<>();
        String userName = "Johnny";
        when(subscribers.get(userName)).thenReturn(subscriber);
        tradingSystem = new TradingSystemBuilder()
                .setUserName(userName)
                .setPassword("Cash")
                .setSubscribers(subscribers)
                .setConnections(connections)
                .setStores(stores)
                .setAuth(auth)
                .build();
    }

    @AfterClass
    public void tearDown() {
        // the number of successfully created stores should be exactly 1 for each 3 trials
        assertEquals((storeCreationTrialNumber.get() / 3) + 1, storeCreationCounter.get());
    }

    @Test(threadPoolSize = 10, invocationCount = 100, timeOut = 1000)
    public void test() throws InvalidActionException {

        // create the store
        try {
            String storeName = "Store " + storeCreationTrialNumber.getAndIncrement() / 3;
            tradingSystem.newStore(subscriber, storeName);
            // this is the expected behavior when the store has been successfully created (no exception thrown)
            storeCreationCounter.getAndIncrement();
        } catch (StoreAlreadyExistsException ignored) {
            // this is the expected behavior when the store already exists
        }
    }
}
