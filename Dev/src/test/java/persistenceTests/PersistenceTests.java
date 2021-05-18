package persistenceTests;

import acceptanceTests.DeliverySystemMock;
import acceptanceTests.Driver;
import acceptanceTests.PaymentSystemMock;
import exceptions.InvalidActionException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.TradingSystemService;
import store.Item;

import javax.persistence.*;

public class PersistenceTests {
    private static EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory("TradingSystem");


    private TradingSystemService service;
    private String storeId1, storeId2; //stores
    private String productId1, productId2, productId3, productId4; //products
    private String admin1Id, founderStore1Id, founderStore2Id, store1Manager1Id, subs1Id, subs2Id, subs3Id, guest1Id; //users Id's
    //users names:
    private String admin1UserName="Admin1", store1FounderUserName="store1FounderUserName", store2FounderUserName="store2FounderUserName",
            store1Manager1UserName="Store1Manager1UserName", subs1UserName = "subs1UserName", subs2UserName = "subs2UserName",
            subs3UserName = "subs3UserName", guest1UserName = "guest1UserName";
    private PaymentSystemMock paymentSystem = (PaymentSystemMock) Driver.getPaymentSystem();
    private DeliverySystemMock deliverySystem = (DeliverySystemMock) Driver.getDeliverySystem();

    @BeforeMethod
    public void setUp() throws Exception {

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
        storeId2 = service.openNewStore(founderStore2Id, "store2");

        //add items for store1:
        productId1 = service.addProductToStore(founderStore1Id, storeId1, "milk", "DairyProducts", "sub1", 10, 6.5);
        productId2 = service.addProductToStore(founderStore1Id, storeId1, "cheese", "DairyProducts", "sub1", 20, 3);
        //add items for store2:
        productId3 = service.addProductToStore(founderStore2Id, storeId2, "milk", "DairyProducts", "sub1", 30, 6.5);
        productId4 = service.addProductToStore(founderStore2Id, storeId2, "baguette", "bread", "", 20, 9);

        service.appointStoreManager(founderStore1Id, store1Manager1UserName, storeId1);

    }
    @Test
    void test1() throws InvalidActionException {
        System.out.println(service.getStoresInfo(admin1Id).toString());
    }

    //todo: persist users, subscribers, notifications, carts, permissions
}
