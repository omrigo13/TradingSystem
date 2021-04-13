package tradingSystem;

import authentication.UserAuthentication;
import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Item;
import store.Store;
import user.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradingSystemTest {

    private TradingSystem tradingSystem;

    @Mock private UserAuthentication auth;
    @Mock private ConcurrentHashMap<String, Subscriber> subscribers;
    @Mock private ConcurrentHashMap<Integer, Store> stores;
    @Mock private Collection<Subscriber> staff;
    @Mock private Map<String, User> connections;
    @Mock private Subscriber subscriber;
    @Mock private User user;
    @Mock private Store store;
    @Mock private Item item;

    @Captor ArgumentCaptor<String> keyCaptor;

    private final String connectionId = "9034580392580932458093248590324850932485";
    private final String userName = "Johnny";
    private final String password = "Cash";
    private final int storeId = 984585;

    @BeforeEach
    void setUp() throws InvalidActionException {

        when(subscribers.get(userName)).thenReturn(subscriber);
        tradingSystem = new TradingSystemBuilder()
                .setUserName(userName)
                .setPassword(password)
                .setSubscribers(subscribers)
                .setConnections(connections)
                .setStores(stores)
                .setAuth(auth)
                .build();
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
        when(connections.get(connectionId)).thenReturn(user);
        when(user.getSubscriber()).thenReturn(subscriber);
        tradingSystem.logout(connectionId);
        verify(connections).put(keyCaptor.capture(), any(User.class));
        assertSame(connectionId, keyCaptor.getValue());
    }

    @Test
    void logoutGuest() throws NotLoggedInException {
        when(connections.get(connectionId)).thenReturn(user);
        doThrow(new NotLoggedInException()).when(user).getSubscriber();
        assertThrows(NotLoggedInException.class, () -> tradingSystem.logout(connectionId));
    }

    @Test
    void newStore() throws InvalidActionException {

        tradingSystem.newStore(subscriber, "Toy Story");
        verify(subscriber).addPermission(OwnerPermission.getInstance(store));
        verify(subscriber).addPermission(ManagerPermission.getInstance(store));
        verify(subscriber).addPermission(ManageInventoryPermission.getInstance(store));
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