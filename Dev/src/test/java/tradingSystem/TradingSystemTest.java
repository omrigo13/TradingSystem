package tradingSystem;

import authentication.UserAuthentication;
import exceptions.InvalidActionException;
import exceptions.InvalidConnectionIdException;
import exceptions.InvalidStoreIdException;
import exceptions.NotLoggedInException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import store.Item;
import store.Store;
import user.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.*;

public class TradingSystemTest {

    private TradingSystem tradingSystem;

    @Mock private UserAuthentication auth;
    @Mock private ConcurrentHashMap<String, Subscriber> subscribers;
    @Mock private ConcurrentHashMap<Integer, Store> stores;
    @Mock private Collection<Subscriber> staff;
    @Mock private ConcurrentHashMap<String, User> connections;
    @Mock private Subscriber subscriber;
    @Mock private User user;
    @Mock private Store store;
    @Mock private Item item;

    @Captor ArgumentCaptor<Store> storeCaptor;

    private final String connectionId = "9034580392580932458093248590324850932485";
    private final String userName = "Johnny";
    private final String password = "Cash";
    private final int storeId = 984585;

    @BeforeMethod
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
        reset(subscribers);
    }

    @Test
    void getUserByConnectionId() throws InvalidActionException {
        when(connections.get(connectionId)).thenReturn(user);
        assertSame(tradingSystem.getUserByConnectionId(connectionId), user);
    }

    @Test
    void getUserByConnectionId_ConnectionIdDoesNotExist() {
        assertThrows(InvalidConnectionIdException.class, () -> tradingSystem.getUserByConnectionId(connectionId));
    }

    @Test
    void getStore() throws InvalidStoreIdException {
        when(stores.get(storeId)).thenReturn(store);
        assertSame(store, tradingSystem.getStore(storeId));
    }

    @Test
    void getStore_InvalidStoreId() {
        assertThrows(InvalidStoreIdException.class, () -> tradingSystem.getStore(storeId));
    }

    @Test
    void register() throws InvalidActionException {
        tradingSystem.register(userName, password);
        verify(auth).register(userName, password);
        verify(subscribers).put(eq(userName), any(Subscriber.class));
    }

    @Test
    void connect() {
        String connectionId = tradingSystem.connect();
        verify(connections).put(anyString(), any(User.class));
        String uuid = java.util.UUID.randomUUID().toString();
        assertNotSame(uuid, connectionId); // verify we got a new uuid
        assertEquals(uuid.length(), connectionId.length()); // verify we got the correct length
    }

    @Test
    void login() throws InvalidActionException {
        when(connections.get(connectionId)).thenReturn(user);
        when(subscribers.get(userName)).thenReturn(subscriber);
        tradingSystem.login(connectionId, userName, password);
        verify(subscriber).makeCart(user);
        verify(connections).put(connectionId, subscriber);
    }

    @Test
    void logoutSubscriber() throws InvalidActionException {
        doReturn(user).when(tradingSystem).getUserByConnectionId(connectionId);
        doReturn(subscriber).when(user).getSubscriber();
        tradingSystem.logout(connectionId);
        verify(connections).put(eq(connectionId), any(User.class));
    }

    @Test
    void logoutGuest() throws InvalidActionException {
        doReturn(user).when(tradingSystem).getUserByConnectionId(connectionId);
        doThrow(NotLoggedInException.class).when(user).getSubscriber();
        assertThrows(NotLoggedInException.class, () -> tradingSystem.logout(connectionId));
    }

    @Test
    void newStore() throws InvalidActionException {

        tradingSystem.newStore(subscriber, "Toy Story");
        verify(stores).put(anyInt(), storeCaptor.capture());
        verify(subscriber).addOwnerPermission(storeCaptor.getValue());
    }

    @Test
    void getStoreStaff() throws InvalidActionException {
        Collection<Subscriber> allSubscribers = new HashSet<>();
        allSubscribers.add(subscriber);
        when(subscribers.values()).thenReturn(allSubscribers);
        when(subscriber.havePermission(ManagerPermission.getInstance(store))).thenReturn(true);
        tradingSystem.getStoreStaff(subscriber, store, staff);
        verify(staff).add(subscriber);
    }

    @Test
    void getItems() {
        String s = "S";
        Double d = 1.0;
        Collection<Store> storesCollection = new LinkedList<>();
        storesCollection.add(store);
        when(stores.values()).thenReturn(storesCollection);
        ConcurrentLinkedQueue<Item> items = new ConcurrentLinkedQueue<>(); // TODO why ConcurrentLinkedQueue!?
        items.add(item);
        when(store.searchAndFilter(s, s, s, d, d, d, d)).thenReturn(items);
        Collection<String> result = tradingSystem.getItems(s, s, s, s, d, d, d, d);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void purchaseCart() {
        //TODO add a test here
    }
}