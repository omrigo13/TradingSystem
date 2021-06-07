package loadAndStressTests;

import authentication.UserAuthentication;
import exceptions.AlreadyManagerException;
import exceptions.InvalidActionException;
import exceptions.NoPermissionException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;
import tradingSystem.TradingSystemBuilder;
import user.Subscriber;
import user.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertFalse;

public class LoadTestGuestsConnect {

    private TradingSystem tradingSystem;
    private final String userName = "Johnny";
    private final String password = "Cash";
    @Mock private ConcurrentHashMap<String, Subscriber> subscribers;
    @Mock private ConcurrentHashMap<String, User> connections;
    @Mock private ConcurrentHashMap<Integer, Store> stores;
    @Mock private UserAuthentication auth;
    @Mock private Subscriber subscriber;

    @BeforeClass
    void setUp() throws InvalidActionException {
        MockitoAnnotations.openMocks(this);

        when(subscribers.get(userName)).thenReturn(subscriber);
        tradingSystem = spy(new TradingSystemBuilder()
                .setUserName(userName)
                .setPassword(password)
                .setSubscribers(subscribers)
                .setConnections(connections)
                .setStores(stores)
                .setAuth(auth)
                .build());

    }

    @Test(threadPoolSize = 1000, invocationCount = 1000, timeOut = 4000)
    public void test() throws InvalidActionException {
        tradingSystem.connect();
    }
}
