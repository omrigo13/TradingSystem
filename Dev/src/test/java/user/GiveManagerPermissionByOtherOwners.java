package user;

import exceptions.AlreadyManagerException;
import exceptions.NoPermissionException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import persistence.RepoMock;
import store.Item;
import store.Store;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.testng.AssertJUnit.assertFalse;

public class GiveManagerPermissionByOtherOwners {

    private Subscriber source1, source2, target;

    private final Store store = mock(Store.class);
    private StorePermission ownerPermission;
    private final AtomicInteger trialNumber = new AtomicInteger();

    @Mock private Collection<HistoryPurchases> itemsPurchased;
    @Mock private LinkedList<String> purchaseHistory;

    @BeforeClass
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RepoMock.enable();
        ownerPermission = OwnerPermission.getInstance(store);
        Set<Permission> targetPermissions = new HashSet<>();
        Set<Permission> source1Permissions = new HashSet<>();
        Set<Permission> source2Permissions = new HashSet<>();
        source1Permissions.add(ownerPermission);
        source2Permissions.add(ownerPermission);
        source1 = spy(new Subscriber(1, "Johnny", source1Permissions, itemsPurchased, purchaseHistory));
        source2 = spy(new Subscriber(2, "Johnny2", source2Permissions, itemsPurchased, purchaseHistory));
        target = spy(new Subscriber(3, "Johnny3", targetPermissions, itemsPurchased, purchaseHistory));
    }

    @Test(threadPoolSize = 10, invocationCount = 1000, timeOut = 10000)
    public void test() throws NoPermissionException {
        try {
            if(trialNumber.getAndIncrement() % 2 == 0) {
                source1.addManagerPermission(target, store);
                assertFalse(source2.havePermission(AppointerPermission.getInstance(target, store)));
                source1.removeManagerPermission(target, store);
            }
            else {
                source2.addManagerPermission(target, store);
                assertFalse(source1.havePermission(AppointerPermission.getInstance(target, store)));
                source2.removeManagerPermission(target, store);
            }
        }
        catch (AlreadyManagerException e) {
            // trying to give manager permission together to the same target subscriber
        }
    }
}
