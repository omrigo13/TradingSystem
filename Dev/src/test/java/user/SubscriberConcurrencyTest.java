package user;

import exceptions.AlreadyOwnerException;
import exceptions.NoPermissionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Item;
import store.Store;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.State.BLOCKED;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class SubscriberConcurrencyTest {

    @Mock private Set<Permission> permissions1;
    @Mock private Set<Permission> permissions2;
    @Mock private Store store;
    @Mock private Map<Store, Collection<Item>> itemsPurchased;
    @Mock private Collection<String> purchaseHistory;

    @Test
    void testPermissionsLocks() throws InterruptedException {
        testPermissionsLocks(false);
    }

    @Test
    void testPermissionsLocks_reversedOrder() throws InterruptedException {
        testPermissionsLocks(true);
    }

    @SuppressWarnings({"SynchronizeOnNonFinalField", "BusyWait"})
    void testPermissionsLocks(boolean reverseOrder) throws InterruptedException {

        // test a situation of 2 subscribers trying to appoint one another as store owners
        // the potentially problematic point is when trying to acquire each other's locks

        CountDownLatch latch = new CountDownLatch(2);

        Subscriber sub1 = new Subscriber(1, "Sub1", permissions1, itemsPurchased, purchaseHistory);
        Subscriber sub2 = new Subscriber(2, "Sub2", permissions2, itemsPurchased, purchaseHistory);

        Thread thread1 = new MyThread("Thread-1", sub1, sub2, latch);
        Thread thread2 = new MyThread("Thread-2", sub2, sub1, latch);

        // lock both permissions objects and try to appoint each other
        synchronized (permissions2) {
            if (reverseOrder) thread2.start(); else thread1.start();

            // busy wait for one of the threads to block
            while (thread1.getState() != BLOCKED && thread2.getState() != BLOCKED)
                Thread.sleep(1);

            if (reverseOrder) thread1.start(); else thread2.start();

            // busy wait for both threads to block
            while (thread1.getState() != BLOCKED || thread2.getState() != BLOCKED)
                Thread.sleep(1);
        }

        // wait for the threads to complete or deadlock
        while(!latch.await(1, TimeUnit.MILLISECONDS)) // 1 millisecond should suffice
            assertFalse(thread1.getState() == BLOCKED && thread2.getState() == BLOCKED, "Deadlock");

        // both threads completed without deadlock
    }

    private class MyThread extends Thread {
        private final Subscriber subscriber;
        private final Subscriber target;
        private final CountDownLatch latch;

        public MyThread(String name, Subscriber subscriber, Subscriber target, CountDownLatch latch) {
            super(name);
            this.subscriber = subscriber;
            this.target = target;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                subscriber.addOwnerPermission(target, store);
            } catch (NoPermissionException | AlreadyOwnerException ignored) {
            }
            latch.countDown();
        }
    }
}