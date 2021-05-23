package robustnessTests;

import acceptanceTests.DeliverySystemMock;
import acceptanceTests.Driver;
import acceptanceTests.PaymentSystemMock;
import exceptions.DeliverySystemException;
import exceptions.InvalidActionException;
import exceptions.ItemNotPurchasedException;
import exceptions.PaymentSystemException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.TradingSystemService;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;
import tradingSystem.TradingSystemImpl;
import user.Basket;
import user.Subscriber;
import user.User;

import java.util.Collection;
import java.util.Map;

import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

public class RobustnessTests {
    private TradingSystemService service;
    private String storeId1, storeId2; //stores
    private String productId1, productId2, productId3, productId4; //products
    private String admin1Id, founderStore1Id, founderStore2Id, store1Manager1Id, subs1Id, subs2Id, subs3Id, guest1Id; //users Id's
    //users names:
    private String admin1UserName="Admin1", store1FounderUserName="store1FounderUserName", store2FounderUserName="store2FounderUserName",
            store1Manager1UserName="Store1Manager1UserName", subs1UserName = "subs1UserName", subs2UserName = "subs2UserName",
            subs3UserName = "subs3UserName", guest1UserName = "guest1UserName";
    private PaymentSystem paymentSystem;
    private DeliverySystem deliverySystem;
    private String card_number = "1234", holder = "a", ccv = "001", id = "000000018", name = "name", address = "address", city = "city", country = "country";
    private int month = 1, year = 2022, zip = 12345;

    //setup with payment system that only throws exceptions
    public void setUpBadPaymentSystem() throws Exception {
        paymentSystem = new PaymentSystemMock2();
        Driver.setPaymentSystem(paymentSystem);
        deliverySystem = Driver.getDeliverySystem();
        //Driver.getPaymentSystem();
        //(DeliverySystemMock) Driver.getDeliverySystem();
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
        productId3 = service.addProductToStore(founderStore2Id, storeId2, "milk", "DairyProducts", "sub1", 30, 6.5);
        productId4 = service.addProductToStore(founderStore2Id, storeId2, "baguette", "bread", "", 20, 9);

        service.appointStoreManager(founderStore1Id, store1Manager1UserName, storeId1);

    }

    //setup with delivery system that only throws exceptions
    public void setUpBadDeliverySystem() throws Exception {
        deliverySystem = new DeliverySystemMock2();
        Driver.setDeliverySystem(deliverySystem);
        paymentSystem = Driver.getPaymentSystem();
        //Driver.getPaymentSystem();
        //(DeliverySystemMock) Driver.getDeliverySystem();
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
        productId3 = service.addProductToStore(founderStore2Id, storeId2, "milk", "DairyProducts", "sub1", 30, 6.5);
        productId4 = service.addProductToStore(founderStore2Id, storeId2, "baguette", "bread", "", 20, 9);

        service.appointStoreManager(founderStore1Id, store1Manager1UserName, storeId1);

    }

    void addToBasketUseCase() throws InvalidActionException {
        service.addItemToBasket(subs1Id, storeId1, productId1, 10);
        service.addItemToBasket(subs1Id, storeId2, productId4, 4);
        service.addItemToBasket(subs2Id, storeId1, productId2, 2);
        //user 1 bought p.1 (milk), p.4 (baguette)
        //user 2 bought p.2 (cheese)
    }

    @Test
    void paymentSystemException() throws Exception{
        setUpBadPaymentSystem();
        addToBasketUseCase();
        assertThrows(PaymentSystemException.class, () -> service.purchaseCart(subs1Id, card_number, month, year, holder, ccv, subs1UserName, subs1UserName, address, city, country, zip));
        assertThrows(PaymentSystemException.class, ()-> service.purchaseCart(subs2Id, card_number, month, year, holder, ccv, subs2UserName, subs2UserName, address, city, country, zip));

        //check that system is still running
        String newStoreId = service.openNewStore(founderStore1Id, "store990");
        service.addProductToStore(founderStore1Id, newStoreId, "robustItem1", "DairyProducts", "sub1", 10, 6.5);
        assertTrue(service.getStoresInfo(admin1Id).toString().contains("robustItem1"));
    }

    @Test
    void deliverySystemException() throws Exception{
        setUpBadDeliverySystem();
        addToBasketUseCase();
        assertThrows(DeliverySystemException.class, () -> service.purchaseCart(subs1Id, card_number, month, year, holder, ccv, subs1UserName, subs1UserName, address, city, country, zip));
        assertThrows(DeliverySystemException.class, ()-> service.purchaseCart(subs2Id, card_number, month, year, holder, ccv, subs2UserName, subs2UserName, address, city, country, zip));

        //check that system is still running
        String newStoreId = service.openNewStore(founderStore1Id, "store991");
        service.addProductToStore(founderStore1Id, newStoreId, "robustItem2", "DairyProducts", "sub1", 10, 6.5);
        assertTrue(service.getStoresInfo(admin1Id).toString().contains("robustItem2"));
    }

    @Test
    void databaseException() throws Exception{
        //todo: write test
    }
}
