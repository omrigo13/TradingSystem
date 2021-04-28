package acceptanceTests;

import exceptions.InvalidActionException;
import exceptions.ItemException;
import exceptions.NoPermissionException;
import exceptions.SubscriberDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TradingSystemService;
import user.Subscriber;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class AcceptanceTestsV2 {
    private static TradingSystemService service;
    private String storeId1, storeId2; //stores
    private String productId1, productId2, productId3, productId4; //products
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
    void assignQuantityPurchasePolicyAndMakePurchase()
    {

    }
}
