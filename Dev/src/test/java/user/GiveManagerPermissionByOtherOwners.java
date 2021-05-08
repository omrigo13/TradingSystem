package user;

import exceptions.AlreadyManagerException;
import exceptions.NoPermissionException;
import exceptions.WrongAmountException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import store.Item;
import store.Store;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;

public class GiveManagerPermissionByOtherOwners {

    private Subscriber source, target;

    @Mock private Store store;
    private final StorePermission ownerPermission = OwnerPermission.getInstance(store);
    private final AtomicInteger permissionSuccessful = new AtomicInteger();

    @Mock private Set<Permission> permissions;
    @Mock private ConcurrentHashMap<Store, Collection<Item>> itemsPurchased;
    @Mock private LinkedList<String> purchaseHistory;

    @BeforeClass
    void setUp() {
        MockitoAnnotations.openMocks(this);

        source = spy(new Subscriber(1, "Johnny", permissions, itemsPurchased, purchaseHistory));
        target = spy(new Subscriber(2, "Johnny2", permissions, itemsPurchased, purchaseHistory));
        when(source.havePermission(ownerPermission)).thenReturn(true);
        source.addOwnerPermission(store);
    }

    @AfterClass
    public void tearDown() {
        // the number of successfully add manager permission should be exactly 1 for each 4 trials
        assertEquals(25, (permissionSuccessful.get() / 4));
    }

    @Test(threadPoolSize = 10, invocationCount = 100, timeOut = 1000)
    public void test() throws NoPermissionException {
        try {
            source.addManagerPermission(target, store);
            if(permissionSuccessful.get() % 4 == 0)
                source.removeManagerPermission(target, store);
            permissionSuccessful.getAndIncrement();
        }
        catch (AlreadyManagerException e) {
            // trying to give manager permission together to the same target subscriber
        }
    }
}
