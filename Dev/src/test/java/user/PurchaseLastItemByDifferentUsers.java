package user;

import exceptions.ItemException;
import exceptions.PolicyException;
import exceptions.WrongAmountException;
import externalServices.DeliveryData;
import externalServices.DeliverySystem;
import externalServices.PaymentData;
import externalServices.PaymentSystem;
import notifications.Observable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import policies.DiscountPolicy;
import policies.PurchasePolicy;
import store.Item;
import store.Store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class PurchaseLastItemByDifferentUsers {

    private final ConcurrentHashMap<Store, Basket> baskets = new ConcurrentHashMap<>();
    private final User user1 = new User(baskets), user2 = new User(baskets);
    private final ConcurrentHashMap<Item, Integer> basketItems = new ConcurrentHashMap<>();
    private final AtomicInteger trialNumber = new AtomicInteger();
    private final AtomicInteger itemsBoughtFromStore = new AtomicInteger();
    private final AtomicInteger itemsAddedToStore = new AtomicInteger();

    int itemID;
    Item item;
    @Mock private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;
    @Mock private PaymentData paymentData;
    @Mock private DeliveryData deliveryData;
    private final PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
    private final DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
    private final Observable observable = mock(Observable.class);

    private final Store store = new Store(0, "eBay", "desc", purchasePolicy, discountPolicy, observable);
    private final Map<Item, Integer> storeItems = store.getItems();
    private final Basket basket = new Basket(store, basketItems);

    public PurchaseLastItemByDifferentUsers() throws ItemException {
    }

    @BeforeClass
    void setUp() throws PolicyException, ItemException {
        MockitoAnnotations.openMocks(this);
        itemID = store.addItem("a", 5.0, "cat", "sub", 0);
        item = store.searchItemById(itemID);
        basketItems.put(store.searchItemById(itemID), 1);
        baskets.put(store, basket);

        when(purchasePolicy.isValidPurchase(basket)).thenReturn(true);
    }

    @AfterClass
    public void tearDown() {
        assertEquals(itemsAddedToStore.get(), itemsBoughtFromStore.get());
    }

    @Test(threadPoolSize = 10, invocationCount = 10000, timeOut = 12000)
    public void test() throws Exception {
        try {
            int trialNumber = this.trialNumber.getAndIncrement();
            User user = trialNumber % 2 == 0 ? user1 : user2;
            if (trialNumber % 2 == 0) {
                //noinspection ConstantConditions
                int currentQuantity = storeItems.compute(item, (k, v) -> v + 1);
                itemsAddedToStore.getAndIncrement();
                assertTrue(currentQuantity > 0);
            }

            user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData);
            itemsBoughtFromStore.getAndIncrement();
        }
        catch (WrongAmountException e) {
            // tried to purchase item but there are not enough in inventory
        }
    }
}
