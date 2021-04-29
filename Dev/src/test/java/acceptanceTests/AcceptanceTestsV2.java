package acceptanceTests;

import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.RegexConversion;
import service.TradingSystemService;
import user.Subscriber;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptanceTestsV2 {
    private static TradingSystemService service;
    private String storeId1, storeId2; //stores
    private String productId1, productId2, productId3, productId4, tomato, corn; //products
    private String admin1Id, founderStore1Id, founderStore2Id, store1Manager1Id, subs1Id, subs2Id, subs3Id, guest1Id; //users Id's
    //users names:
    private String admin1UserName="Admin1", store1FounderUserName="store1FounderUserName", store2FounderUserName="store2FounderUserName",
            store1Manager1UserName="Store1Manager1UserName", subs1UserName = "subs1UserName", subs2UserName = "subs2UserName",
            subs3UserName = "subs3UserName", guest1UserName = "guest1UserName";
    @BeforeEach
    void setUp() throws InvalidActionException {

        service = Driver.getService("Admin1", "ad123"); //params are details of system manager to register into user authenticator
        admin1Id = service.connect();
        founderStore1Id = service.connect();
        founderStore2Id = service.connect();
        store1Manager1Id = service.connect();
        subs1Id = service.connect();
        subs2Id = service.connect();
        subs3Id = service.connect();
        guest1Id = service.connect();


        service.register("store1FounderUserName", "1234");
        service.register("store2FounderUserName", "1234");
        service.register("Store1Manager1UserName", "1234");
        service.register("subs1UserName", "1234");
        service.register("subs2UserName", "1234");
        service.register("subs3UserName", "1234");


        service.login(admin1Id, "Admin1", "ad123");
        service.login(founderStore1Id, "store1FounderUserName", "1234"); //storeId1 founder
        service.login(founderStore2Id, "store2FounderUserName", "1234"); //storeId2 founder
        service.login(store1Manager1Id, "Store1Manager1UserName", "1234");
        service.login(subs1Id, "subs1UserName", "1234");
        service.login(subs2Id, "subs2UserName", "1234");
        service.login(subs3Id, "subs3UserName", "1234");


        storeId1 = service.openNewStore(founderStore1Id, "store1");

        productId1 = service.addProductToStore(founderStore1Id, storeId1, "milk", "DairyProducts", "sub1", 10, 6.5);

        productId2 = service.addProductToStore(founderStore1Id, storeId1, "cheese", "DairyProducts", "sub1", 20, 3);

        tomato = service.addProductToStore(founderStore1Id, storeId1, "tomato", "vegetables", "sub1", 20, 8.5);
        corn = service.addProductToStore(founderStore1Id, storeId1, "corn", "vegetables", "sub1", 30, 12.0);

        storeId2 = service.openNewStore(founderStore2Id, "store2");
        productId3 = service.addProductToStore(founderStore2Id, storeId2, "milk", "DairyProducts", "sub2", 30, 6.5);
        productId4 = service.addProductToStore(founderStore2Id, storeId2, "baguette", "bread", "", 20, 9);

        service.appointStoreManager(founderStore1Id, store1Manager1UserName, storeId1);
    }

    @Test
    void purchaseEmptyCart() throws InvalidActionException {
        assertEquals(0, service.showCart(founderStore1Id).size());
        assertDoesNotThrow(() -> service.purchaseCart(founderStore1Id));
        assertEquals(0, service.getPurchaseHistory(founderStore1Id).size()); // TODO should be fixed in the code
    }

    @Test
    void purchaseOneItemFromOneStore() throws InvalidActionException {
        service.addItemToBasket(founderStore1Id, storeId1, productId1, 2);
        assertEquals(1, service.showCart(founderStore1Id).size());
        assertDoesNotThrow(() -> service.purchaseCart(founderStore1Id));
        assertEquals(0, service.showCart(founderStore1Id).size());
        assertEquals(1, service.getPurchaseHistory(founderStore1Id).size());
        assertTrue(service.getPurchaseHistory(founderStore1Id).toString().contains("13"));
    }

    @Test
    void purchaseTwoItemsFromDifferentTwoStores() throws InvalidActionException {
        service.addItemToBasket(founderStore1Id, storeId1, productId1, 2);
        service.addItemToBasket(founderStore1Id, storeId2, productId4, 3);
        assertEquals(2, service.showCart(founderStore1Id).size());
        assertDoesNotThrow(() -> service.purchaseCart(founderStore1Id));
        assertEquals(0, service.showCart(founderStore1Id).size());
        assertEquals(1, service.getPurchaseHistory(founderStore1Id).size());
        assertTrue(service.getPurchaseHistory(founderStore1Id).toString().contains("13") &&
                service.getPurchaseHistory(founderStore1Id).toString().contains("27"));
    }

    @Test
    void validAllowManagerToGetHistory() throws InvalidActionException {
        service.addItemToBasket(founderStore1Id, storeId1, productId1, 2);
        service.addItemToBasket(founderStore1Id, storeId1, productId2, 3);
        service.purchaseCart(founderStore1Id);

        assertThrows(NoPermissionException.class, () -> service.getSalesHistoryByStore(store1Manager1Id, storeId1));
        assertDoesNotThrow(() -> service.allowManagerToGetHistory(founderStore1Id, storeId1, store1Manager1UserName));
        assertDoesNotThrow(() -> service.getSalesHistoryByStore(store1Manager1Id, storeId1));
        assertEquals(1, service.getSalesHistoryByStore(store1Manager1Id, storeId1).size());
        assertTrue(service.getSalesHistoryByStore(store1Manager1Id, storeId1).toString().contains("milk"));
        assertTrue(service.getSalesHistoryByStore(store1Manager1Id, storeId1).toString().contains("cheese"));
    }

    @Test
    void notValidAllowManagerToGetHistory() throws InvalidActionException {
        service.addItemToBasket(founderStore1Id, storeId1, productId1, 2);
        service.addItemToBasket(founderStore1Id, storeId1, productId2, 3);
        service.purchaseCart(founderStore1Id);

        assertThrows(NoPermissionException.class, () ->service.allowManagerToGetHistory(store1Manager1Id, storeId1, store2FounderUserName));
        assertThrows(SubscriberDoesNotExistException.class, () ->service.allowManagerToGetHistory(store1Manager1Id, storeId1, guest1Id));
        assertThrows(SubscriberDoesNotExistException.class, () ->service.allowManagerToGetHistory(founderStore1Id, storeId1, guest1Id));
        assertThrows(NoPermissionException.class, () ->service.allowManagerToGetHistory(store1Manager1Id, storeId1, subs2UserName));
    }

    @Test
    void validAllowToEditPurchasesPoliciesByAdmin() throws InvalidActionException {
        assertDoesNotThrow(() -> service.getStorePolicies(admin1Id, storeId1));
        assertDoesNotThrow(() -> service.getStorePolicies(admin1Id, storeId2));

        Collection<String> store1Items = new ArrayList<>();
        store1Items.add(productId1);
        store1Items.add(productId2);

        int quantityPolicy, basketPolicy, timePolicy, andPolicy, orPolicy, xorPolicy;
        quantityPolicy = service.makeQuantityPolicy(admin1Id, storeId1, store1Items, 1, 0);
        basketPolicy = service.makeBasketPurchasePolicy(admin1Id, storeId1, 50);
        timePolicy = service.makeTimePolicy(admin1Id, storeId1, store1Items, "00:00");

        andPolicy = service.andPolicy(admin1Id, storeId1, quantityPolicy, basketPolicy);
        orPolicy = service.orPolicy(admin1Id, storeId1, quantityPolicy, timePolicy);
        xorPolicy = service.xorPolicy(admin1Id, storeId1, basketPolicy, timePolicy);

        assertDoesNotThrow(() -> service.assignStorePurchasePolicy(andPolicy, admin1Id, storeId1));

        assertDoesNotThrow(() -> service.removePolicy(admin1Id, storeId1, quantityPolicy));
        assertDoesNotThrow(() -> service.removePolicy(admin1Id, storeId1, basketPolicy));
        assertDoesNotThrow(() -> service.removePolicy(admin1Id, storeId1, andPolicy));
    }

    @Test
    void validAllowToEditDiscountPoliciesByAdmin() throws InvalidActionException {
        assertDoesNotThrow(() -> service.getStoreDiscounts(admin1Id, storeId1));
        assertDoesNotThrow(() -> service.getStoreDiscounts(admin1Id, storeId2));

        Collection<String> store1Items = new ArrayList<>();
        store1Items.add(productId1);
        store1Items.add(productId2);

        int quantityDiscount1, quantityDiscount2, plusDiscount, maxDiscount;
        quantityDiscount1 = service.makeQuantityDiscount(admin1Id, storeId1, 10, store1Items, null);
        quantityDiscount2 = service.makeQuantityDiscount(admin1Id, storeId1, 20, store1Items, null);

        maxDiscount = service.makeMaxDiscount(admin1Id, storeId1, quantityDiscount1, quantityDiscount2);
        plusDiscount = service.makePlusDiscount(admin1Id, storeId1, quantityDiscount1, maxDiscount);

        assertDoesNotThrow(() -> service.assignStoreDiscountPolicy(plusDiscount, admin1Id, storeId1));

        assertDoesNotThrow(() -> service.removeDiscount(admin1Id, storeId1, quantityDiscount1));
        assertDoesNotThrow(() -> service.removeDiscount(admin1Id, storeId1, quantityDiscount2));
        assertDoesNotThrow(() -> service.removeDiscount(admin1Id, storeId1, maxDiscount));
        assertDoesNotThrow(() -> service.removeDiscount(admin1Id, storeId1, plusDiscount));
    }

    @Test
    void validAllowToEditPurchasePoliciesByStoreOwner() throws InvalidActionException {
        assertDoesNotThrow(() -> service.getStorePolicies(founderStore1Id, storeId1));

        Collection<String> store1Items = new ArrayList<>();
        store1Items.add(productId1);
        store1Items.add(productId2);

        int quantityPolicy, basketPolicy, timePolicy, andPolicy, orPolicy, xorPolicy;
        quantityPolicy = service.makeQuantityPolicy(founderStore1Id, storeId1, store1Items, 1, 0);
        basketPolicy = service.makeBasketPurchasePolicy(founderStore1Id, storeId1, 50);
        timePolicy = service.makeTimePolicy(founderStore1Id, storeId1, store1Items, "00:00");

        andPolicy = service.andPolicy(founderStore1Id, storeId1, quantityPolicy, basketPolicy);
        orPolicy = service.orPolicy(founderStore1Id, storeId1, quantityPolicy, timePolicy);
        xorPolicy = service.xorPolicy(founderStore1Id, storeId1, basketPolicy, timePolicy);

        assertDoesNotThrow(() -> service.assignStorePurchasePolicy(andPolicy, founderStore1Id, storeId1));

        assertDoesNotThrow(() -> service.removePolicy(founderStore1Id, storeId1, quantityPolicy));
        assertDoesNotThrow(() -> service.removePolicy(founderStore1Id, storeId1, basketPolicy));
        assertDoesNotThrow(() -> service.removePolicy(founderStore1Id, storeId1, andPolicy));
    }

    @Test
    void validAllowToEditDiscountPoliciesByStoreOwner() throws InvalidActionException {
        assertDoesNotThrow(() -> service.getStoreDiscounts(founderStore1Id, storeId1));

        Collection<String> store1Items = new ArrayList<>();
        store1Items.add(productId1);
        store1Items.add(productId2);

        int quantityDiscount1, quantityDiscount2, plusDiscount, maxDiscount;
        quantityDiscount1 = service.makeQuantityDiscount(founderStore1Id, storeId1, 10, store1Items, null);
        quantityDiscount2 = service.makeQuantityDiscount(founderStore1Id, storeId1, 20, store1Items, null);

        maxDiscount = service.makeMaxDiscount(founderStore1Id, storeId1, quantityDiscount1, quantityDiscount2);
        plusDiscount = service.makePlusDiscount(founderStore1Id, storeId1, quantityDiscount1, maxDiscount);

        assertDoesNotThrow(() -> service.assignStoreDiscountPolicy(plusDiscount, founderStore1Id, storeId1));

        assertDoesNotThrow(() -> service.removeDiscount(founderStore1Id, storeId1, quantityDiscount1));
        assertDoesNotThrow(() -> service.removeDiscount(founderStore1Id, storeId1, quantityDiscount2));
        assertDoesNotThrow(() -> service.removeDiscount(founderStore1Id, storeId1, maxDiscount));
        assertDoesNotThrow(() -> service.removeDiscount(founderStore1Id, storeId1, plusDiscount));
    }

    @Test
    void validAllowToEditPurchasesPoliciesByManager() throws InvalidActionException {
        service.allowManagerToEditPolicies(founderStore1Id, storeId1, store1Manager1UserName);
        assertDoesNotThrow(() -> service.getStorePolicies(store1Manager1Id, storeId1));

        Collection<String> store1Items = new ArrayList<>();
        store1Items.add(productId1);
        store1Items.add(productId2);

        int quantityPolicy, basketPolicy, timePolicy, andPolicy, orPolicy, xorPolicy;
        quantityPolicy = service.makeQuantityPolicy(store1Manager1Id, storeId1, store1Items, 1, 0);
        basketPolicy = service.makeBasketPurchasePolicy(store1Manager1Id, storeId1, 50);
        timePolicy = service.makeTimePolicy(store1Manager1Id, storeId1, store1Items, "00:00");

        andPolicy = service.andPolicy(store1Manager1Id, storeId1, quantityPolicy, basketPolicy);
        orPolicy = service.orPolicy(store1Manager1Id, storeId1, quantityPolicy, timePolicy);
        xorPolicy = service.xorPolicy(store1Manager1Id, storeId1, basketPolicy, timePolicy);

        assertDoesNotThrow(() -> service.assignStorePurchasePolicy(andPolicy, store1Manager1Id, storeId1));

        assertDoesNotThrow(() -> service.removePolicy(store1Manager1Id, storeId1, quantityPolicy));
        assertDoesNotThrow(() -> service.removePolicy(store1Manager1Id, storeId1, basketPolicy));
        assertDoesNotThrow(() -> service.removePolicy(store1Manager1Id, storeId1, andPolicy));
    }

    @Test
    void validAllowToEditDiscountsPoliciesByManager() throws InvalidActionException {
        service.allowManagerToEditPolicies(founderStore1Id, storeId1, store1Manager1UserName);
        assertDoesNotThrow(() -> service.getStoreDiscounts(store1Manager1Id, storeId1));

        Collection<String> store1Items = new ArrayList<>();
        store1Items.add(productId1);
        store1Items.add(productId2);

        int quantityDiscount1, quantityDiscount2, plusDiscount, maxDiscount;
        quantityDiscount1 = service.makeQuantityDiscount(store1Manager1Id, storeId1, 10, store1Items, null);
        quantityDiscount2 = service.makeQuantityDiscount(store1Manager1Id, storeId1, 20, store1Items, null);

        maxDiscount = service.makeMaxDiscount(store1Manager1Id, storeId1, quantityDiscount1, quantityDiscount2);
        plusDiscount = service.makePlusDiscount(store1Manager1Id, storeId1, quantityDiscount1, maxDiscount);

        assertDoesNotThrow(() -> service.assignStoreDiscountPolicy(plusDiscount, store1Manager1Id, storeId1));

        assertDoesNotThrow(() -> service.removeDiscount(store1Manager1Id, storeId1, quantityDiscount1));
        assertDoesNotThrow(() -> service.removeDiscount(store1Manager1Id, storeId1, quantityDiscount2));
        assertDoesNotThrow(() -> service.removeDiscount(store1Manager1Id, storeId1, maxDiscount));
        assertDoesNotThrow(() -> service.removeDiscount(store1Manager1Id, storeId1, plusDiscount));
    }

    @Test
    void notValidAllowManagerOrStoreOwnerToEditPurchases() throws InvalidActionException {
        assertThrows(NoPermissionException.class, () -> service.getStorePolicies(store1Manager1Id, storeId1));
        assertThrows(NoPermissionException.class, () -> service.getStorePolicies(founderStore2Id, storeId1));

        Collection<String> store1Items = new ArrayList<>();
        store1Items.add(productId1);
        store1Items.add(productId2);

        assertThrows(NoPermissionException.class, () -> service.makeQuantityPolicy(founderStore2Id, storeId1, store1Items, 1, 0));
        assertThrows(NoPermissionException.class, () -> service.makeBasketPurchasePolicy(founderStore2Id, storeId1, 50));
        assertThrows(NoPermissionException.class, () -> service.makeTimePolicy(founderStore2Id, storeId1, store1Items, "00:00"));

        assertThrows(NoPermissionException.class, () -> service.makeQuantityPolicy(store1Manager1Id, storeId1, store1Items, 1, 0));
        assertThrows(NoPermissionException.class, () -> service.makeBasketPurchasePolicy(store1Manager1Id, storeId1, 50));
        assertThrows(NoPermissionException.class, () -> service.makeTimePolicy(store1Manager1Id, storeId1, store1Items, "00:00"));

        assertThrows(NoPermissionException.class, () -> service.assignStorePurchasePolicy(service.makeQuantityPolicy(store1Manager1Id, storeId1, store1Items, 1, 0), store1Manager1Id, storeId1));

        assertThrows(NoPermissionException.class, () -> service.removePolicy(store1Manager1Id, storeId1, 0));
        assertThrows(NoPermissionException.class, () -> service.removePolicy(store1Manager1Id, storeId1, 0));
        assertThrows(NoPermissionException.class, () -> service.removePolicy(store1Manager1Id, storeId1, 0));
    }

    @Test
    void notValidAllowManagerOrStoreOwnerToEditDiscounts() {
        assertThrows(NoPermissionException.class, () -> service.getStoreDiscounts(store1Manager1Id, storeId1));
        assertThrows(NoPermissionException.class, () -> service.getStoreDiscounts(founderStore2Id, storeId1));

                Collection<String> store1Items = new ArrayList<>();
        store1Items.add(productId1);
        store1Items.add(productId2);

        assertThrows(NoPermissionException.class, () -> service.makeQuantityDiscount(founderStore2Id, storeId1, 10, store1Items, null));
        assertThrows(NoPermissionException.class, () -> service.makeQuantityDiscount(founderStore2Id, storeId1, 20, store1Items, null));

        assertThrows(NoPermissionException.class, () -> service.makeQuantityDiscount(store1Manager1Id, storeId1, 10, store1Items, null));
        assertThrows(NoPermissionException.class, () -> service.makeQuantityDiscount(store1Manager1Id, storeId1, 20, store1Items, null));

        assertThrows(NoPermissionException.class, () -> service.makeMaxDiscount(store1Manager1Id, storeId1, 0, 1));
        assertThrows(NoPermissionException.class, () -> service.makePlusDiscount(store1Manager1Id, storeId1, 0, 1));

        assertThrows(NoPermissionException.class, () -> service.assignStoreDiscountPolicy(2, store1Manager1Id, storeId1));

        assertThrows(NoPermissionException.class, () -> service.removeDiscount(store1Manager1Id, storeId1, 0));
        assertThrows(NoPermissionException.class, () -> service.removeDiscount(store1Manager1Id, storeId1, 1));
        assertThrows(NoPermissionException.class, () -> service.removeDiscount(store1Manager1Id, storeId1, 2));
        assertThrows(NoPermissionException.class, () -> service.removeDiscount(store1Manager1Id, storeId1, 3));
    }

    @Test
    void basketMustHaveLessThen5KgTomatoesPurchasePolicy() throws InvalidActionException {
        Collection<String> items = new ArrayList<>();
        items.add(tomato);

        int quantityPolicy = service.makeQuantityPolicy(founderStore1Id, storeId1, items, 0, 5);
        service.assignStorePurchasePolicy(quantityPolicy, founderStore1Id, storeId1);

        service.addItemToBasket(store1Manager1Id, storeId1, tomato, 4);
        service.purchaseCart(store1Manager1Id);
        assertTrue(service.getPurchaseHistory(store1Manager1Id).toString().contains("34"));

        service.addItemToBasket(store1Manager1Id, storeId1, tomato, 6);
        assertThrows(PolicyException.class, () -> service.purchaseCart(store1Manager1Id));
        assertFalse(service.getPurchaseHistory(store1Manager1Id).toString().contains("51"));

        service.addItemToBasket(guest1Id, storeId1, tomato, 4);
        service.purchaseCart(guest1Id);

        String goodPurchase = service.getSalesHistoryByStore(founderStore1Id, storeId1).toString();
        assertTrue(goodPurchase.indexOf("34") != goodPurchase.lastIndexOf("34") && goodPurchase.contains("34"));

        service.addItemToBasket(guest1Id, storeId1, tomato, 6);
        assertThrows(PolicyException.class, () -> service.purchaseCart(guest1Id));
        assertFalse(service.getSalesHistoryByStore(founderStore1Id, storeId1).toString().contains("51"));

    }

    @Test
    void tomatoCanBePurchasedOnlyAfter10AMPurchasePolicy() throws InvalidActionException {
        Collection<String> items = new ArrayList<>();
        items.add(tomato);

        int timePolicy = service.makeTimePolicy(founderStore1Id, storeId1, items, "10:00");
        service.assignStorePurchasePolicy(timePolicy, founderStore1Id, storeId1);

        service.addItemToBasket(store1Manager1Id, storeId1, tomato, 4);
        service.purchaseCart(store1Manager1Id);
        assertTrue(service.getPurchaseHistory(store1Manager1Id).toString().contains("34"));
    }

    @Test
    void basketMustHaveLessThen5KgTomatoesAndAtLeast2CornsPurchasePolicy() throws InvalidActionException {
        Collection<String> tomatoes = new ArrayList<>();
        tomatoes.add(tomato);

        Collection<String> corns = new ArrayList<>();
        corns.add(corn);

        int tomatoQuantityPolicy = service.makeQuantityPolicy(founderStore1Id, storeId1, tomatoes, 0, 5);
        int cornQuantityPolicy = service.makeQuantityPolicy(founderStore1Id, storeId1, corns, 2, 0);
        int tomatoAndCornPolicy = service.andPolicy(founderStore1Id, storeId1, tomatoQuantityPolicy, cornQuantityPolicy);

        service.assignStorePurchasePolicy(tomatoAndCornPolicy, founderStore1Id, storeId1);

        service.addItemToBasket(store1Manager1Id, storeId1, tomato, 4);
        assertThrows(QuantityPolicyException.class, () -> service.purchaseCart(store1Manager1Id));

        service.addItemToBasket(store1Manager1Id, storeId1, corn, 2);
        service.addItemToBasket(store1Manager1Id, storeId1, tomato, 2);
        assertThrows(AndPolicyException.class, () -> service.purchaseCart(store1Manager1Id));

        service.updateProductAmountInBasket(store1Manager1Id, storeId1, tomato, 5);
        service.purchaseCart(store1Manager1Id);
    }

    @Test
    void basketCanHave5KgTomatoesOrMoreOnlyIfThereIsAtLeast1CornPurchasePolicy() {

    }
}
