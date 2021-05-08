package user;

import exceptions.*;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import org.mockito.*;
import org.testng.annotations.*;
import policies.PurchasePolicy;
import store.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;

public class PurchaseLastItemByDifferentUsers {

    private User user;
    private ConcurrentHashMap<Store, Basket> baskets;
    private HashMap<Item, Integer> items;
    private final AtomicInteger purchaseSuccessful = new AtomicInteger();

    @Mock private Store store;
    @Mock private Basket basket;
    @Spy private Item item;
    @Mock private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;
    @Mock private PurchasePolicy purchasePolicy;

    @BeforeClass
    void setUp() throws PolicyException {
        MockitoAnnotations.openMocks(this);

        baskets = new ConcurrentHashMap<>();
        items = new HashMap<>();

        items.put(item, 5);

        user = spy(new User(baskets));
        baskets.put(store, basket);

        when(store.getPurchasePolicy()).thenReturn(purchasePolicy);
        when(purchasePolicy.isValidPurchase(basket)).thenReturn(true);
        when(store.getItems()).thenReturn(items);
        when(basket.getItems()).thenReturn(items);
    }

    @AfterClass
    public void tearDown() {
        // the number of successfully purchases should be exactly 1 for each 4 trials
        assertEquals(25, (purchaseSuccessful.get() / 4));
    }

    @Test(threadPoolSize = 10, invocationCount = 100, timeOut = 1000)
    public void test() throws Exception{
        try {
            user.purchaseCart(paymentSystem, deliverySystem);
            if(purchaseSuccessful.get() % 4 == 0)
                items.replace(item, 5);
            purchaseSuccessful.getAndIncrement();
        }
        catch (WrongAmountException e) {
            // trying to purchase together the same item, no amount exception
        }
    }
}
