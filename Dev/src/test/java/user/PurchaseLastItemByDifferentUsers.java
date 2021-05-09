package user;

import exceptions.*;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import notifications.Observable;
import org.mockito.*;
import org.testng.annotations.*;
import policies.DiscountPolicy;
import policies.PurchasePolicy;
import store.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class PurchaseLastItemByDifferentUsers {

    private final ConcurrentHashMap<Store, Basket> baskets = new ConcurrentHashMap<>();
    private final User user1 = new User(baskets), user2 = new User(baskets);
    private final ConcurrentHashMap<Item, Integer> items = new ConcurrentHashMap<>();
    private final AtomicInteger trialNumber = new AtomicInteger();
    private final AtomicInteger itemsBoughtFromStore = new AtomicInteger();
    private final AtomicInteger itemsAddedToStore = new AtomicInteger(1);

    int itemID;
    Item item;
    @Mock private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;
    private final PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
    private final DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
    private final Observable observable = mock(Observable.class);

    private final Store store = new Store(0, "eBay", "desc", purchasePolicy, discountPolicy, observable);
    private final Basket basket = new Basket(store, items);

    public PurchaseLastItemByDifferentUsers() throws ItemException {
    }

    @BeforeClass
    void setUp() throws PolicyException, ItemException {
        MockitoAnnotations.openMocks(this);
        itemID = store.addItem("a", 5.0, "cat", "sub", 1);
        item = store.searchItemById(itemID);
        items.put(store.searchItemById(itemID), 1);
        baskets.put(store, basket);

        when(purchasePolicy.isValidPurchase(basket)).thenReturn(true);
    }

    @AfterClass
    public void tearDown() {
        assertEquals(itemsBoughtFromStore.get(), itemsAddedToStore.get() - 1);
    }

    @Test(threadPoolSize = 10, invocationCount = 100, timeOut = 1000)
    public void test() throws Exception{
        try {
            User user = trialNumber.getAndIncrement() % 2 == 0 ? user1 : user2;
            store.changeItem(itemID, null, 1, null);
            user.purchaseCart(paymentSystem, deliverySystem);
            itemsBoughtFromStore.getAndIncrement();
            int currentQuantity = store.getItems().get(item);
            itemsAddedToStore.addAndGet(1 - currentQuantity);
            System.out.println("Trial " + trialNumber.get() + " succeeded to purchase");
        }
        catch (WrongAmountException e) {
            // trying to purchase together the same item, no amount exception
            System.out.println("Trial " + trialNumber.get() + " failed to purchase");
        }
    }
}
