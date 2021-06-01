package acceptanceTests;

import exceptions.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.TradingSystemService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.testng.Assert.assertThrows;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class AcceptanceTestsV3 {
    private static TradingSystemService service;

    private String admin1Id, founderStore1Id, founderStore2Id, store1Manager1Id, store2Manager1Id, subs1Id, guest1Id;
    private String storeId1, storeId2;
    private String productId1, productId2, tomato, corn, milk, baguette;
    private final String store1Manager1UserName = "Store1Manager1UserName", store2Manager1UserName = "Store2Manager1UserName", subs1UserName = "subs1UserName";
    private final String card_number = "1234", holder = "a", ccv = "001", id = "000000018", name = "name", address = "address", city = "city", country = "country";
    private final int month = 1, year = 2022, zip = 12345;
    private final String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    private int quantityPolicy, quantityPolicy2;

    @BeforeMethod
    void setUp() throws InvalidActionException {
        service = Driver.getService("Admin1", "ad123"); //params are details of system manager to register into user authenticator
        admin1Id = service.connect();
        service.login(admin1Id, "Admin1", "ad123");
    }

    void setUpGuest() throws InvalidActionException {
        guest1Id = service.connect();
    }

    void setUpSubscriber1() throws InvalidActionException {
        subs1Id = service.connect();
        service.register("subs1UserName", "1234");
        service.login(subs1Id, "subs1UserName", "1234");
    }

    void setUpStore1Founder() throws InvalidActionException {
        founderStore1Id = service.connect();
        service.register("store1FounderUserName", "1234");
        service.login(founderStore1Id, "store1FounderUserName", "1234"); //storeId1 founder
    }

    void setUpStore1Manager() throws InvalidActionException {
        store1Manager1Id = service.connect();
        service.register("Store1Manager1UserName", "1234");
        service.login(store1Manager1Id, "Store1Manager1UserName", "1234");
        service.appointStoreManager(founderStore1Id, store1Manager1UserName, storeId1);
    }

    void setUpStore1() throws InvalidActionException {
        setUpStore1Founder();
        storeId1 = service.openNewStore(founderStore1Id, "store1");
        productId1 = service.addProductToStore(founderStore1Id, storeId1, "milk", "dairy", "sub1", 15, 6.5);
        productId2 = service.addProductToStore(founderStore1Id, storeId1, "cheese", "dairy", "sub1", 20, 3);
        tomato = service.addProductToStore(founderStore1Id, storeId1, "tomato", "vegetables", "red", 20, 8.5);
        setUpStore1Manager();

    }

    void setUpStore2Founder() throws InvalidActionException {
        founderStore2Id = service.connect();
        service.register("store2FounderUserName", "1234");
        service.login(founderStore2Id, "store2FounderUserName", "1234"); //storeId2 founder
    }

    void setUpStore2Manager() throws InvalidActionException {
        store2Manager1Id = service.connect();
        service.register("Store2Manager1UserName", "1234");
        service.login(store2Manager1Id, "Store2Manager1UserName", "1234");
        service.appointStoreManager(founderStore2Id, store2Manager1UserName, storeId2);
    }

    void setUpStore2() throws InvalidActionException {
        setUpStore2Founder();
        storeId2 = service.openNewStore(founderStore2Id, "store2");
        milk = service.addProductToStore(founderStore2Id, storeId2, "milk", "DairyProducts", "sub2", 30, 6.5);
        baguette = service.addProductToStore(founderStore2Id, storeId2, "baguette", "bread", "", 20, 9);
        corn = service.addProductToStore(founderStore2Id, storeId2, "corn", "vegetables", "yellow", 30, 12.0);
        setUpStore2Manager();
    }

    void purchaseTwoItemsFromDifferentTwoStores() throws InvalidActionException {
        setUpStore1();
        setUpStore2();
        service.addItemToBasket(founderStore1Id, storeId1, productId1, 2);
        service.addItemToBasket(founderStore1Id, storeId2, baguette, 3);
        assertEquals(2, service.showCart(founderStore1Id).size());
        service.purchaseCart(founderStore1Id, card_number, month, year, holder, ccv, id, name, address, city, country, zip);
        assertEquals(0, service.showCart(founderStore1Id).size());
        assertEquals(1, service.getPurchaseHistory(founderStore1Id).size());
        assertTrue(service.getPurchaseHistory(founderStore1Id).toString().contains("13") &&
                service.getPurchaseHistory(founderStore1Id).toString().contains("27"));
    }

    @Test
    void get_total_income_by_guest() throws InvalidActionException {
        purchaseTwoItemsFromDifferentTwoStores();
        setUpGuest();

        assertThrows(NotLoggedInException.class, () -> service.getTotalIncomeByStorePerDay(guest1Id, storeId1, today));
        assertThrows(NotLoggedInException.class, () -> service.getTotalIncomeByStorePerDay(guest1Id, storeId2, today));
        assertThrows(NotLoggedInException.class, () -> service.getTotalIncomeByAdminPerDay(guest1Id, today));
    }

    @Test
    void get_total_income_by_owner_or_manager_with_no_permission() throws InvalidActionException {
        purchaseTwoItemsFromDifferentTwoStores();

        assertThrows(NoPermissionException.class, () -> service.getTotalIncomeByStorePerDay(store1Manager1Id, storeId1, today));
        assertThrows(NoPermissionException.class, () -> service.getTotalIncomeByStorePerDay(store1Manager1Id, storeId2, today));
        assertThrows(NoPermissionException.class, () -> service.getTotalIncomeByStorePerDay(store2Manager1Id, storeId1, today));
        assertThrows(NoPermissionException.class, () -> service.getTotalIncomeByStorePerDay(store2Manager1Id, storeId2, today));
        assertThrows(NoPermissionException.class, () -> service.getTotalIncomeByStorePerDay(founderStore1Id, storeId2, today));
        assertThrows(NoPermissionException.class, () -> service.getTotalIncomeByStorePerDay(founderStore2Id, storeId1, today));
        assertThrows(NoPermissionException.class, () -> service.getTotalIncomeByAdminPerDay(store1Manager1Id, today));
        assertThrows(NoPermissionException.class, () -> service.getTotalIncomeByAdminPerDay(store2Manager1Id, today));
    }

    @Test
    void get_total_income_by_owner_with_permission() throws InvalidActionException {
        purchaseTwoItemsFromDifferentTwoStores();

        assertTrue(service.getTotalIncomeByStorePerDay(founderStore1Id, storeId1, today).contains("13.0"));
        assertTrue(service.getTotalIncomeByStorePerDay(founderStore2Id, storeId2, today).contains("27.0"));
        assertTrue(service.getTotalIncomeByStorePerDay(admin1Id, storeId1, today).contains("13.0"));
        assertTrue(service.getTotalIncomeByStorePerDay(admin1Id, storeId2, today).contains("27.0"));
    }

    @Test
    void get_total_income_by_admin() throws InvalidActionException {
        purchaseTwoItemsFromDifferentTwoStores();

        assertThrows(NoPermissionException.class, () -> service.getTotalIncomeByAdminPerDay(founderStore1Id, today));
        assertThrows(NoPermissionException.class, () -> service.getTotalIncomeByAdminPerDay(founderStore2Id, today));
        assertTrue(service.getTotalIncomeByAdminPerDay(admin1Id, today).toString().contains("store1") &&
                service.getTotalIncomeByAdminPerDay(admin1Id, today).toString().contains("store2"));
    }

    @Test
    void add_item_to_basket_by_offer_with_guest() throws InvalidActionException {
        setUpGuest();
        setUpStore1();

        assertThrows(NotLoggedInException.class, () -> service.addItemToBasketByOffer(guest1Id, storeId1, productId1, 5, 3.0));
    }

    @Test
    void add_item_to_basket_by_offer_with_subscriber() throws InvalidActionException {
        setUpSubscriber1();
        setUpStore1();

        service.addItemToBasketByOffer(subs1Id, storeId1, productId2, 4, 1.5);

        assertTrue(service.getOffersByStore(founderStore1Id, storeId1).toString().contains("offer id: 0, user: subs1UserName, item: cheese, quantity: 4, price: 1.5"));
    }

    @Test
    void get_store_offers_by_guest() throws InvalidActionException {
        setUpGuest();
        setUpStore1();
        setUpStore2();

        assertThrows(NotLoggedInException.class, () -> service.getOffersByStore(guest1Id, storeId1));
        assertThrows(NotLoggedInException.class, () -> service.getOffersByStore(guest1Id, storeId2));
    }

    @Test
    void get_store_offers_by_owner_or_manager_without_permission() throws InvalidActionException {
        setUpStore1();
        setUpStore2();

        assertThrows(NoPermissionException.class, () -> service.getOffersByStore(store1Manager1Id, storeId1));
        assertThrows(NoPermissionException.class, () -> service.getOffersByStore(store1Manager1Id, storeId2));
        assertThrows(NoPermissionException.class, () -> service.getOffersByStore(store2Manager1Id, storeId1));
        assertThrows(NoPermissionException.class, () -> service.getOffersByStore(store2Manager1Id, storeId2));
        assertThrows(NoPermissionException.class, () -> service.getOffersByStore(founderStore1Id, storeId2));
        assertThrows(NoPermissionException.class, () -> service.getOffersByStore(founderStore2Id, storeId1));
    }

    @Test
    void get_store_offers_by_owner_with_permission() throws InvalidActionException {
        setUpStore1();
        setUpStore2();
        setUpSubscriber1();

        service.addItemToBasketByOffer(subs1Id, storeId1, productId2, 4, 1.5);
        service.addItemToBasketByOffer(subs1Id, storeId2, milk, 4, 3.5);

        assertTrue(service.getOffersByStore(founderStore1Id, storeId1).toString().contains("user: subs1UserName") &&
                service.getOffersByStore(founderStore1Id, storeId1).toString().contains("price: 1.5"));
        assertTrue(service.getOffersByStore(founderStore2Id, storeId2).toString().contains("user: subs1UserName") &&
                service.getOffersByStore(founderStore2Id, storeId2).toString().contains("price: 3.5"));
        assertTrue(service.getOffersByStore(admin1Id, storeId1).toString().contains("user: subs1UserName") &&
                service.getOffersByStore(admin1Id, storeId1).toString().contains("price: 1.5"));
        assertTrue(service.getOffersByStore(admin1Id, storeId2).toString().contains("user: subs1UserName") &&
                service.getOffersByStore(admin1Id, storeId2).toString().contains("price: 3.5"));
    }

    @Test
    void approve_offer_by_guest() throws InvalidActionException {
        setUpGuest();
        setUpStore1();
        setUpSubscriber1();

        service.addItemToBasketByOffer(subs1Id, storeId1, productId2, 4, 1.5);

        assertThrows(NotLoggedInException.class, () -> service.approveOffer(guest1Id, storeId1, 0, 0.0));
    }

    @Test
    void approve_offer_by_owner_or_manager_without_permission() throws InvalidActionException {
        setUpStore1();
        setUpStore2();

        assertThrows(NoPermissionException.class, () -> service.approveOffer(store1Manager1Id, storeId1, 0 , 0.0));
        assertThrows(NoPermissionException.class, () -> service.approveOffer(store1Manager1Id, storeId2, 0 , 0.0));
        assertThrows(NoPermissionException.class, () -> service.approveOffer(store2Manager1Id, storeId1, 0 , 0.0));
        assertThrows(NoPermissionException.class, () -> service.approveOffer(store2Manager1Id, storeId2, 0 , 0.0));
        assertThrows(NoPermissionException.class, () -> service.approveOffer(founderStore1Id, storeId2, 0 , 0.0));
        assertThrows(NoPermissionException.class, () -> service.approveOffer(founderStore2Id, storeId1, 0 , 0.0));
        assertThrows(NoPermissionException.class, () -> service.approveOffer(store1Manager1Id, storeId2, 0 , 0.0));
        assertThrows(NoPermissionException.class, () -> service.approveOffer(store2Manager1Id, storeId1, 0 , 0.0));
    }

    @Test
    void approve_offer_by_owner_or_manager_with_permission() throws InvalidActionException {
        setUpStore1();
        setUpStore2();
        setUpSubscriber1();

        service.addItemToBasketByOffer(subs1Id, storeId1, productId2, 4, 1.5);
        service.addItemToBasketByOffer(subs1Id, storeId2, milk, 4, 3.5);
        service.allowManagerToUpdateProducts(founderStore1Id, storeId1, store1Manager1UserName);
        service.allowManagerToUpdateProducts(founderStore2Id, storeId2, store2Manager1UserName);

        assertThrows(OfferNotExistsException.class, () -> service.approveOffer(founderStore1Id, storeId1, 2 , 0.0));
        assertThrows(OfferNotExistsException.class, () -> service.approveOffer(founderStore2Id, storeId2, 2 , 0.0));
        assertThrows(OfferNotExistsException.class, () -> service.approveOffer(admin1Id, storeId1, 2 , 0.0));
        assertThrows(OfferNotExistsException.class, () -> service.approveOffer(admin1Id, storeId2, 2 , 0.0));

        assertEquals(0, service.showBasket(subs1Id, storeId1).size());
        assertEquals(0, service.showBasket(subs1Id, storeId2).size());

        service.approveOffer(founderStore1Id, storeId1, 0 , 0.0);
        service.approveOffer(store1Manager1Id, storeId1, 0 , 0.0);
        service.approveOffer(founderStore2Id, storeId2, 0 , 0.0);
        service.approveOffer(store2Manager1Id, storeId2, 0 , 0.0);

        assertTrue(service.showBasket(subs1Id, storeId1).toString().contains("Item: cheese Quantity: 4"));
        assertTrue(service.showBasket(subs1Id, storeId2).toString().contains("Item: milk Quantity: 4"));
    }

    @Test
    void purchase_approved_offer_does_not_meet_purchase_policy() throws InvalidActionException {
        setUpStore1();
        setUpStore2();
        setUpGuest();
        setUpSubscriber1();

        Collection<String> items = new ArrayList<>();
        items.add(tomato);
        Collection<String> items2 = new ArrayList<>();
        items2.add(corn);

        quantityPolicy = service.makeQuantityPolicy(founderStore1Id, storeId1, items, 2, 0);
        quantityPolicy2 = service.makeQuantityPolicy(founderStore2Id, storeId2, items2, 2, 0);
        service.assignStorePurchasePolicy(quantityPolicy, founderStore1Id, storeId1);
        service.assignStorePurchasePolicy(quantityPolicy2, founderStore2Id, storeId2);

        service.addItemToBasketByOffer(subs1Id, storeId1, tomato, 3, 2.0);
        service.addItemToBasketByOffer(subs1Id, storeId2, corn, 1, 3.2);
        service.approveOffer(founderStore1Id, storeId1, 0, 0.0);
        service.approveOffer(founderStore2Id, storeId2, 0, 0.0);
        service.addItemToBasket(guest1Id, storeId1, tomato, 1);
        service.addItemToBasket(guest1Id, storeId2, corn, 2);

        assertThrows(PolicyException.class, () -> service.purchaseCart(guest1Id, card_number, month, year, holder, ccv, id, name, address, city, country, zip));
        assertThrows(PolicyException.class, () -> service.purchaseCart(subs1Id, card_number, month, year, holder, ccv, id, name, address, city, country, zip));
    }

    @Test
    void purchase_approved_offer_with_good_details() throws InvalidActionException {
        setUpStore1();
        setUpStore2();
        setUpGuest();
        setUpSubscriber1();

        Collection<String> items = new ArrayList<>();
        items.add(tomato);
        Collection<String> items2 = new ArrayList<>();
        items2.add(corn);

        quantityPolicy = service.makeQuantityPolicy(founderStore1Id, storeId1, items, 2, 0);
        quantityPolicy2 = service.makeQuantityPolicy(founderStore2Id, storeId2, items2, 2, 0);
        service.assignStorePurchasePolicy(quantityPolicy, founderStore1Id, storeId1);
        service.assignStorePurchasePolicy(quantityPolicy2, founderStore2Id, storeId2);

        service.addItemToBasketByOffer(subs1Id, storeId1, tomato, 3, 2.0);
        service.addItemToBasketByOffer(subs1Id, storeId2, corn, 2, 3.2);
        service.approveOffer(founderStore1Id, storeId1, 0, 0.0);
        service.approveOffer(founderStore2Id, storeId2, 0, 0.0);
        service.addItemToBasket(guest1Id, storeId1, tomato, 4);
        service.addItemToBasket(guest1Id, storeId2, corn, 2);

        service.purchaseCart(guest1Id, card_number, month, year, holder, ccv, id, name, address, city, country, zip);
        assertTrue(service.getSalesHistoryByStore(founderStore1Id, storeId1).toString().contains("Item: tomato Price: 8.5 Quantity: 4"));
        assertTrue(service.getSalesHistoryByStore(founderStore2Id, storeId2).toString().contains("Item: corn Price: 12.0 Quantity: 2"));

        service.purchaseCart(subs1Id, card_number, month, year, holder, ccv, id, name, address, city, country, zip);
        assertTrue(service.getSalesHistoryByStore(founderStore1Id, storeId1).toString().contains("Item: tomato Price: 2.0 Quantity: 3"));
        assertTrue(service.getSalesHistoryByStore(founderStore2Id, storeId2).toString().contains("Item: corn Price: 3.2 Quantity: 2"));
    }

    @Test
    void purchase_countered_offer_with_good_details() throws InvalidActionException {
        //TODO fix counter offer and let the user approve the offer and add more tests after that
        //TODO is it possible to update cart after offer?

        setUpStore1();
        setUpStore2();
        setUpSubscriber1();

        Collection<String> items = new ArrayList<>();
        items.add(tomato);
        Collection<String> items2 = new ArrayList<>();
        items2.add(corn);

        quantityPolicy = service.makeQuantityPolicy(founderStore1Id, storeId1, items, 2, 0);
        quantityPolicy2 = service.makeQuantityPolicy(founderStore2Id, storeId2, items2, 2, 0);
        service.assignStorePurchasePolicy(quantityPolicy, founderStore1Id, storeId1);
        service.assignStorePurchasePolicy(quantityPolicy2, founderStore2Id, storeId2);

        service.addItemToBasketByOffer(subs1Id, storeId1, tomato, 3, 2.0);
        service.addItemToBasketByOffer(subs1Id, storeId2, corn, 2, 3.2);
        service.approveOffer(founderStore1Id, storeId1, 0, 3.0);
        service.approveOffer(founderStore2Id, storeId2, 0, 4.2);

        service.purchaseCart(subs1Id, card_number, month, year, holder, ccv, id, name, address, city, country, zip);
        assertTrue(service.getSalesHistoryByStore(founderStore1Id, storeId1).toString().contains("Item: tomato Price: 3.0 Quantity: 3"));
        assertTrue(service.getSalesHistoryByStore(founderStore2Id, storeId2).toString().contains("Item: corn Price: 4.2 Quantity: 2"));
    }
}
