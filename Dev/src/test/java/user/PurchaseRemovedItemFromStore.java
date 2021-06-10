package user;

import exceptions.*;
import externalServices.*;
import notifications.Observable;
import org.mockito.*;
import org.testng.annotations.*;
import policies.*;
import store.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class PurchaseRemovedItemFromStore {

    private Subscriber owner;

    private final ConcurrentHashMap<Store, Basket> baskets = new ConcurrentHashMap<>();
    private final User user = new User(baskets);
    private final ConcurrentHashMap<Item, Integer> basketItems = new ConcurrentHashMap<>();

    int itemID;
    Item item;
    @Mock private ConcurrentHashMap<Store, Collection<Item>> itemsPurchased;
    @Mock private LinkedList<String> purchaseHistory;
    @Mock private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;
    @Mock private PaymentData paymentData;
    @Mock private DeliveryData deliveryData;
    private final PurchasePolicy purchasePolicy = mock(PurchasePolicy.class);
    private final DiscountPolicy discountPolicy = mock(DiscountPolicy.class);
    private final Observable observable = mock(Observable.class);

    private final Store store = new Store(0, "eBay", "desc", purchasePolicy, discountPolicy, observable);
    private final Map<Integer, Item> storeItems = store.getItems();
    private final StorePermission ownerPermission = OwnerPermission.getInstance(store);
    private final StorePermission manageInventory = ManageInventoryPermission.getInstance(store);
    private final Basket basket = new Basket(store, basketItems);
    private final AtomicInteger trialNumber = new AtomicInteger();
    private final AtomicInteger itemsBoughtFromStore = new AtomicInteger();
    private final AtomicInteger itemsAddedToStore = new AtomicInteger();

    public PurchaseRemovedItemFromStore() throws ItemException {
    }

    @BeforeClass
    void setUp() throws ItemException, PolicyException {
        MockitoAnnotations.openMocks(this);
        itemID = store.addItem("a", 5.0, "cat", "sub", 0);
        item = store.searchItemById(itemID);
        basketItems.put(store.searchItemById(itemID), 1);
        baskets.put(store, basket);

        Set<Permission> storeOwnerPermissions = new HashSet<>();
        storeOwnerPermissions.add(ownerPermission);
        storeOwnerPermissions.add(manageInventory);
        owner = spy(new Subscriber(1, "Johnny", storeOwnerPermissions, itemsPurchased, purchaseHistory));

        when(purchasePolicy.isValidPurchase(basket)).thenReturn(true);
    }

    @AfterClass
    public void tearDown() {
        assertTrue(itemsBoughtFromStore.get() <= itemsAddedToStore.get());
    }

    @Test(threadPoolSize = 10, invocationCount = 10000, timeOut = 15000)
    public void test() throws Exception{
        try {
            if(trialNumber.getAndIncrement() % 2 == 0) {
                storeItems.compute(item.getId(), (k, v) -> {
                    if (v == null)
                        item.setAmount(1);
                    else
                        item.setAmount(item.getAmount() + 1);
                    return item;
                });
                itemsAddedToStore.getAndIncrement();
                user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData);
                itemsBoughtFromStore.getAndIncrement();
            }
            else {
                owner.removeStoreItem(store, itemID);
            }
        }
        catch (ItemNotFoundException | ItemAlreadyExistsException | WrongAmountException e) {
            // trying to remove and purchase item together
        }
    }
}
