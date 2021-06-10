package persistence;
import acceptanceTests.Driver;
import exceptions.InvalidActionException;
import externalServices.DeliverySystemMock;
import externalServices.PaymentSystemMock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.stylesheets.LinkStyle;
import service.TradingSystemService;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;
import user.Basket;
import user.Subscriber;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PersistenceTests {

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
    private String card_number = "1234", holder = "a", ccv = "001", id = "000000018", name = "name", address = "address", city = "city", country = "country";
    private int month = 1, year = 2022, zip = 12345;

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

        service.logout(subs3Id);
        storeId1 = service.openNewStore(founderStore1Id, "store1");
        storeId2 = service.openNewStore(founderStore2Id, "store2");

        //add items for store1:
        productId1 = service.addProductToStore(founderStore1Id, storeId1, "milk_store1", "DairyProducts", "sub1", 10, 6.5);
        productId2 = service.addProductToStore(founderStore1Id, storeId1, "cheese_store1", "DairyProducts", "sub1", 20, 3);
        //add items for store2:
        productId3 = service.addProductToStore(founderStore2Id, storeId2, "milk_store2", "DairyProducts", "sub1", 30, 6.5);
        productId4 = service.addProductToStore(founderStore2Id, storeId2, "baguette_store2", "bread", "", 20, 9);

//        service.deleteProductFromStore(founderStore1Id, storeId1, productId1);

        System.out.println("pid1="+productId1+ " pid2="+productId2+ " pid3="+productId3);
        service.appointStoreManager(founderStore1Id, store1Manager1UserName, storeId1);

        service.openNewStore(subs1Id, "store3");

        service.getItems("fff", "", "", "", 3.0, 3.0, 3.0, 3.0);

    }

    @Test
    void review_item() throws InvalidActionException {
        service.writeOpinionOnProduct(subs2Id, storeId1, productId2, "reviewwww!!");

        //purchase notification test
//        service.addItemToBasket(subs1Id, storeId1, productId1, 10);
        service.addItemToBasket(subs1Id, storeId2, productId4, 4);
        service.addItemToBasket(subs2Id, storeId1, productId2, 2);
        service.purchaseCart(subs1Id, card_number, month, year, holder, ccv, subs1UserName, subs1UserName, address, city, country, zip);
        Collection<String> items_discount = new ArrayList<>();
        items_discount.add(productId2);
        items_discount.add(productId4);
        service.makeQuantityDiscount(founderStore1Id, storeId1, 1 ,items_discount, null );

        service.makeQuantityPolicy(founderStore1Id, storeId1,items_discount, 1, 100);


//        service.addItemToBasketByOffer(founderStore2Id, storeId1, productId1, 2, 5);
//        service.approveOffer(founderStore1Id, storeId1, 0, -1.0);

//        service.addItemToBasketByOffer(founderStore1Id, storeId2, productId4, 2, 1);
//        service.approveOffer(founderStore2Id, storeId2, 0, 6.0);

//        service.removeManager(founderStore1Id, storeId1, store1Manager1UserName);
        service.appointStoreOwner(founderStore1Id, subs2UserName, storeId1);


        List<Item> items = Repo.getInstance().getItems();
        List<Subscriber> subs = Repo.getInstance().getSubscribers();
        List<Store> stores = Repo.getInstance().getStores();
        List<Basket> baskets = Repo.getInstance().getBaskets();


        System.out.println("");
    }

    //todo: persist users, subscribers, notifications, carts, permissions
    //todo: check why Admin1 is not a Subscriber in TradingSystem.connections
}
