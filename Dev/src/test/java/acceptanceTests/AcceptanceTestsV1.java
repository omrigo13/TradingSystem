package acceptanceTests;

import exceptions.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.TradingSystemService;

import java.util.Collection;

import static org.testng.Assert.assertThrows;
import static org.testng.Assert.expectThrows;
import static org.testng.AssertJUnit.*;

public class AcceptanceTestsV1 {
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

        productId1 = service.addProductToStore(founderStore1Id, storeId1, "milk", "DairyProducts", "sub1", 10, 6.5);

        productId2 = service.addProductToStore(founderStore1Id, storeId1, "cheese", "DairyProducts", "sub1", 20, 3);

        storeId2 = service.openNewStore(founderStore2Id, "store2");
        productId3 = service.addProductToStore(founderStore2Id, storeId2, "milk", "DairyProducts", "sub1", 30, 6.5);
        productId4 = service.addProductToStore(founderStore2Id, storeId2, "baguette", "bread", "", 20, 9);

        service.appointStoreManager(founderStore1Id, store1Manager1UserName, storeId1);


    }

//    @Test
//    void initializeSystemWithGoodUserDetails() {
//        assertDoesNotThrow(() -> new TradingSystemBuilder().setUserName("Admin1").setPassword("ad123").build());
//    }

//    @Test
//    void initializeSystemNotExistedUser() {
//        assertThrows(SubscriberDoesNotExistException.class, () -> new TradingSystemBuilder().setUserName("OzMadmoni").setPassword("abc").build());
//        assertThrows(SubscriberDoesNotExistException.class, () -> new TradingSystemBuilder().setUserName("").setPassword("abc12345").build());
//    }

    @Test
    void connectGuest() throws Exception{
        String s = "";
        s = service.connect();
        assertTrue(s!=null && s.length() > 0);
        String s2 = "";
        s2 = service.connect();
        assertTrue(s2!= null && s2.length() > 0);
        assertTrue(!(s.equals(s2)));
    }

    void registerUserCase() throws Exception {
        service.register("LidorRubi", "123456");
        service.register("asdasd", "123456");
    }

    @Test
    void registerGoodDetails() throws Exception{
        registerUserCase();
    }

//    @Test
//    void registerWrongDetails() throws Exception{
//        assertThrows(Exception.class, () -> service.register(null, "123456"));
//        assertThrows(Exception.class, () -> service.register("asdasd", null));
//    }

    @Test
    void registerSubscriberAlreadyExist() throws Exception{
//        service.register("AAA",  "123");
//        assertThrows(SubscriberAlreadyExistsException.class, () -> service.register("AAA",  "123"));
        registerGoodDetails();
        assertThrows(SubscriberAlreadyExistsException.class, () -> registerGoodDetails());
    }

    @Test
    void validlogin() throws Exception{
        registerUserCase();

        String id1 = service.connect();
        String id2 = service.connect();

        service.login(id1, "LidorRubi", "123456");
        service.login(id2, "asdasd", "123456");

    }

//    @Test
//    void alreadyLoggedIn() throws Exception{
//        String id1 = service.connect();
//        String id2 = service.connect();
//        service.register("tempUser1", "1234");
//
//        service.login(id1, "tempUser1", "1234");
//        assertThrows(NoPermissionException.class, () -> service.login(id2, "tempUser1", "1234"));
//    }

    @Test
    void subscriberNotExistLogin() throws Exception{
        String id1 = service.connect();
        assertThrows(SubscriberDoesNotExistException.class, () -> service.login(id1, "user999", "1234"));
    }

    @Test
    void wrongPasswordLogin() throws Exception{
        String id1 = service.connect();
        String id2 = service.connect();
        registerUserCase();

        assertThrows(WrongPasswordException.class, () -> service.login(id2, "asdasd", "1"));
    }


    @Test
    void validLogout() throws Exception{
        service.openNewStore(subs3Id, "temp store1");
        service.logout(subs3Id);
        assertThrows(NotLoggedInException.class, () -> service.openNewStore(subs3Id, "temp store2"));

    }

    @Test
    void userNotExistLogout() {
        assertThrows(InvalidConnectionIdException.class, () -> service.logout("user999"));
    }

    @Test
    void alreadyLoggedOut() throws InvalidActionException {
        service.logout(subs3Id);
        assertThrows(NotLoggedInException.class, () -> service.logout(subs3Id));
    }

    @Test
    void getItemsByKeyWord() throws Exception{
        assertTrue(!service.getItems("milk", null, null, null, null, null, null, null).isEmpty());
        assertTrue(service.getItems("milk", null, null, null, null, null, null, null).size() == 2);
        assertTrue(service.getItems("milk", null, null, null, null, null, null, null).toString().contains("milk"));

    }

    @Test
    void getItemsByProductName() throws Exception {
        Collection<String> items = service.getItems("", "milk", null, null, null, null, null, null);
        assertEquals(items.size(), 2);
        for (String item: items) {
            assertTrue(item.contains("milk"));
        }
    }

    @Test
    void getItemsByNameNotExist() throws Exception {
        Collection<String> items = service.getItems("", "butter", null, null, null, null, null, null);
        assertEquals(items.size(), 0);
    }

    @Test
    void getItemsByCategory() throws Exception {
        Collection<String> items = service.getItems("", "", "bread", null, null, null, null, null);
        assertTrue(items.size() == 1);
        assertTrue(items.toString().contains("baguette"));
    }

    @Test
    void getItemsByCategoryNotExist() throws Exception {
        Collection<String> items = service.getItems("", "", "bottle", null, null, null, null, null);
        assertTrue(items.size() == 0);
    }

    @Test
    void getItemsByPrice() throws Exception {
        Collection<String> collect = service.getItems("", "", "", null, null, null, 1000.0, 0.5);
        assertTrue(collect.size() == 4);
        assertTrue(collect.toString().contains("milk"));
        assertTrue(collect.toString().contains("cheese"));
        assertTrue(collect.toString().contains("baguette"));
    }

    @Test
    void getItemsByNegativePrice() throws Exception {
        Collection<String> collect = service.getItems("", "", "", null, null, null, -0.5, -2.5);
        assertTrue(collect.size() == 0);
    }

    @Test
    void getItemsByNotExistPriceRange() throws Exception {
        Collection<String> collect = service.getItems("", "", "", null, null, null, 200.0, 100.0);
        assertTrue(collect.size() == 0);
    }

    @Test
    void validAddItemToBasket() throws Exception{
        Collection<String> basket = service.showBasket(store1Manager1Id, storeId1);
        assertTrue(basket.size() == 0);
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 2);
        basket = service.showBasket(store1Manager1Id, storeId1);
        assertTrue(basket.size() == 1);
        assertTrue(basket.toString().contains("milk"));

    }

    @Test
    void notValidAddItemToBasket() throws Exception{
        assertThrows(InvalidStoreIdException.class, () -> service.addItemToBasket(store1Manager1Id, "99", productId1, 2));
        assertThrows(ItemNotFoundException.class, () -> service.addItemToBasket(store1Manager1Id, storeId1, "99", 2));
    }

    @Test
    void showCart() throws Exception{
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        Collection<String> collect = service.showCart(store1Manager1Id);
        assertTrue(collect.size() == 3);
        assertTrue(collect.toString().contains("milk"));
        assertTrue(collect.toString().contains("cheese"));
        assertTrue(service.showCart(subs3Id).isEmpty());
    }

    @Test
    void showBasket() throws Exception{
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        Collection<String> s1 = service.showBasket(store1Manager1Id,storeId1);
        assertTrue(s1 != null && !s1.isEmpty());
        assertTrue(s1.toString().contains("milk") && s1.toString().contains("cheese"));
        Collection<String> s2 = service.showBasket(store1Manager1Id,storeId2);
        assertTrue(s2 != null && !s2.isEmpty());
        assertTrue(s2.toString().contains("milk"));
        assertTrue(service.showBasket(subs3Id, storeId1).isEmpty());
    }

    @Test
    void updateProductAmountInBasket() throws Exception{
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        Collection<String> s1 = service.showBasket(store1Manager1Id,storeId1);
        assertTrue(s1 != null && !s1.isEmpty() && s1.toString().contains("milk") && s1.toString().contains("cheese"));
        service.updateProductAmountInBasket(store1Manager1Id, storeId1, productId1, 0);
        s1 = service.showBasket(store1Manager1Id,storeId1);
        assertTrue(s1 != null && !s1.isEmpty() && s1.toString().contains("milk") && s1.toString().contains("cheese"));
        service.updateProductAmountInBasket(store1Manager1Id, storeId2, productId4, 1 );    // productId4 not added by store1Manager1Id to his basket
        Collection<String> str = service.showBasket(store1Manager1Id, storeId2);
        assertTrue(str.toString().contains("baguette"));
    }

    void addToBasketUseCase() throws InvalidActionException {
        service.addItemToBasket(subs1Id, storeId1, productId1, 10);
        service.addItemToBasket(subs1Id, storeId2, productId4, 4);
        service.addItemToBasket(subs2Id, storeId1, productId2, 2);
        //user 1 bought p.1 (milk), p.4 (baguette)
        //user 2 bought p.2 (cheese)
    }

    @Test
    void validPurchaseCart() throws InvalidActionException {
        //todo: fix Payment and Delivery system mocks and parameters
        addToBasketUseCase(); //run use case
        assertFalse(paymentSystem.getPayments().keySet().contains(subs1UserName));
        assertFalse(deliverySystem.getDeliveries().keySet().contains(subs1UserName));

        service.purchaseCart(subs1Id);
        assertTrue(paymentSystem.getPayments().keySet().contains(subs1UserName));
        assertTrue(paymentSystem.getPayments().get(subs1UserName).get(0) == 101);
        assertTrue(deliverySystem.getDeliveries().keySet().contains(subs1UserName));

        assertFalse(deliverySystem.getDeliveries().keySet().contains(subs2UserName));
        service.purchaseCart(subs2Id);
        assertTrue(paymentSystem.getPayments().keySet().contains(subs2UserName));
        assertTrue(paymentSystem.getPayments().get(subs2UserName).get(0) == 6);
        assertTrue(deliverySystem.getDeliveries().keySet().contains(subs2UserName));

    }

    @Test
    void purchaseCartWrongAmount() throws InvalidActionException {
//        paymentSystemMock.setSucceed(false); //invalid purchase
        addToBasketUseCase(); //run use case
        service.purchaseCart(subs1Id);
        service.addItemToBasket(subs3Id, storeId1, productId1, 1);
        WrongAmountException e = expectThrows(WrongAmountException.class, () -> service.purchaseCart(subs3Id)); //amount in store is currently 0, thus cannot make purchase
        assertEquals("there is not enough from the item", e.getMessage());
        assertFalse(paymentSystem.getPayments().keySet().contains(subs3UserName));
        assertFalse(deliverySystem.getDeliveries().keySet().contains(subs3UserName));
    }

    @Test
    void getPurchaseHistory() throws Exception{
        addToBasketUseCase();

        //user 1 bought p.1 (milk), p.4 (baguette)
        //user 2 bought p.2 (cheese)
        service.purchaseCart(subs1Id);
        assertTrue(service.getPurchaseHistory(subs1Id).toString().contains("milk"));
        assertTrue(service.getPurchaseHistory(subs1Id).toString().contains("baguette"));
        service.addItemToBasket(subs1Id, storeId1, productId2, 1);
        service.purchaseCart(subs1Id);
        Collection<String> str = service.getPurchaseHistory(subs1Id);
        assertTrue(str.size() == 2);
        assertTrue(service.getPurchaseHistory(subs1Id).toString().contains("cheese"));

    }

    void purchaseUserCase2() throws InvalidActionException {
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
    }

    @Test
    void validWriteOpinionOnProduct() throws InvalidActionException {
        purchaseUserCase2();
        service.purchaseCart(store1Manager1Id);
        service.writeOpinionOnProduct(store1Manager1Id, storeId1, productId1, "desc example1");
        service.writeOpinionOnProduct(store1Manager1Id, storeId1, productId2, "desc example2");
        service.writeOpinionOnProduct(store1Manager1Id, storeId2, productId3, "desc example3");

    }

    @Test
    void writeOpinionOnProductNotPurchased() throws InvalidActionException {
        purchaseUserCase2();
        service.purchaseCart(store1Manager1Id);
        assertThrows(ItemNotPurchasedException.class, () -> service.writeOpinionOnProduct(store1Manager1Id, storeId2, productId4, "opinion1"));
    }

    @Test
    void writeOpinionOnProductNotExist() throws InvalidActionException {
        purchaseUserCase2();
        service.purchaseCart(store1Manager1Id);
        assertThrows(ItemNotFoundException.class, () -> service.writeOpinionOnProduct(store1Manager1Id, storeId2, "30", "opinion1")); //no such productId in store inventory
        assertThrows(InvalidStoreIdException.class, () -> service.writeOpinionOnProduct(store1Manager1Id, "99", productId1, "opinion1")); //no such storeId "abc"
    }

    @Test
    void writeOpinionOnProductWrongDesc() throws InvalidActionException {
        purchaseUserCase2();
        service.purchaseCart(store1Manager1Id);

        assertThrows(Exception.class, () -> service.writeOpinionOnProduct(store1Manager1Id, storeId1, productId1, null)); //null opinion
        assertThrows(Exception.class, () -> service.writeOpinionOnProduct(store1Manager1Id, storeId1, productId2, "")); //empty opinion
    }

    @Test
    void getStoresInfo() throws Exception{
        String adminConnect = service.connect();
        service.login(adminConnect, admin1UserName, "ad123");
        assertTrue(service.getStoresInfo(adminConnect).size()>0); //there are 2 stores opened
    }

    @Test
    void getStoresInfoWithoutPermissions() throws Exception{
        //id4 is a store manager of storeId1.
        assertThrows(NoPermissionException.class, () -> service.getStoresInfo(founderStore1Id)); //id2 is only a store owner and not a system manager
        assertThrows(NoPermissionException.class, () -> service.getStoresInfo(store1Manager1Id)); //id4 is only a store manager and not a system manager
        assertThrows(NoPermissionException.class, () -> service.getStoresInfo(subs1Id)); //id5 is only a guest and not a system manager

    }

    @Test
    void validGetItemsByStore() throws Exception{
        Collection<String> s1 = service.getItemsByStore(founderStore1Id, storeId1);
        Collection<String> s2 = service.getItemsByStore(founderStore2Id, storeId2);
        assertTrue(s1.size() == 2);
        assertTrue(s2.size() == 2);
        assertTrue(s1.toString().contains("milk"));
        assertTrue(s1.toString().contains("cheese"));
        assertTrue(s2.toString().contains("milk"));
        assertTrue(s2.toString().contains("baguette"));
    }

    @Test
    void wrongGetItemsByStore() throws Exception{
        //invokers without permissions
        assertThrows(NoPermissionException.class, () -> service.getItemsByStore(founderStore1Id, storeId2));
        assertThrows(NoPermissionException.class, () -> service.getItemsByStore(store1Manager1Id, storeId2));
        assertThrows(NoPermissionException.class, () -> service.getItemsByStore(subs1Id, storeId2));

        //no such storeId
        assertThrows(Exception.class, () -> service.getItemsByStore(founderStore1Id, "abc"));
    }

    @Test
    void openNewStore() throws Exception{
        String newStoreId1 = service.openNewStore(founderStore1Id, "newStore1"); //a store founder opens another store
        assertTrue(newStoreId1 != null && !newStoreId1.isEmpty());
        String newStoreId2 = service.openNewStore(store1Manager1Id, "newStore2"); //a store manager opens a store
        assertTrue(newStoreId2 != null && !newStoreId2.isEmpty());
        String newStoreId3 = service.openNewStore(subs1Id, "newStore3"); //a subscriber opens a store
        assertTrue(newStoreId3 != null && !newStoreId3.isEmpty());
    }

    @Test
    void openNewStoreWithGuest() throws Exception{
        //checking NotLoggedInException since guest cannot perform this task
        assertThrows(NotLoggedInException.class, () -> service.openNewStore(guest1Id, "newStore20"));
    }

    @Test
   void openNewStoreWithWrongName() throws Exception{
        assertThrows(WrongNameException.class, () -> service.openNewStore(founderStore1Id, null)); //null store name
        assertThrows(WrongNameException.class, () -> service.openNewStore(founderStore1Id, "")); //empty store name
    }

    @Test
    void validAppointStoreManager() throws Exception{
        assertThrows(NoPermissionException.class, () -> service.getItemsByStore(founderStore2Id, storeId1));
        service.appointStoreManager(founderStore1Id, store2FounderUserName, storeId1);
        service.getItemsByStore(founderStore2Id, storeId1);

        assertThrows(NoPermissionException.class, () -> service.getItemsByStore(subs1Id, storeId1));
        service.appointStoreManager(founderStore1Id, subs1UserName, storeId1);
        service.getItemsByStore(subs1Id, storeId1);

        assertThrows(NoPermissionException.class, () -> service.getItemsByStore(subs1Id, storeId2));
        service.appointStoreManager(founderStore2Id, subs1UserName, storeId2);
        service.getItemsByStore(subs1Id, storeId2);

    }

    @Test
    void appointGuestAsStoreManager() throws Exception{
        assertThrows(SubscriberDoesNotExistException.class, () -> service.appointStoreManager(founderStore1Id, guest1UserName, storeId1));
    }

    @Test
    void appointAnAlreadyStoreManager() throws Exception{
        assertThrows(AlreadyManagerException.class, () -> service.appointStoreManager(founderStore1Id, store1Manager1UserName, storeId1));
        //test circular appoint:
        assertThrows(NoPermissionException.class, () -> service.appointStoreManager(store1Manager1Id, store1Manager1UserName, storeId1));

    }

    @Test
    void wrongAppointStoreManager() throws Exception{
        assertThrows(NoPermissionException.class, () -> service.appointStoreManager(founderStore2Id, subs1UserName, storeId1)); //founderStore2Id is not an owner at storeId1
        assertThrows(NoPermissionException.class, () -> service.appointStoreManager(store1Manager1Id, subs2UserName, storeId1)); //store1Manager1Id is not an owner at storeId1

    }

    @Test
    void validAddProductToStore() throws Exception{
        String prod1 = service.addProductToStore(founderStore1Id, storeId1, "butter", "DiaryProducts", "", 10, 7.5);
        assertTrue(prod1 != null && !prod1.isEmpty());
        assertTrue(service.getItemsByStore(founderStore1Id, storeId1).toString().contains("butter"));
    }

    @Test
    void wrongAddProductToStore() throws Exception{
        assertThrows(InvalidStoreIdException.class, () -> service.addProductToStore(founderStore1Id, "99", "butter", "DiaryProducts", "", 10, 7.5)); //"abc" is not a storeId
        assertThrows(ItemException.class, () -> service.addProductToStore(founderStore1Id, storeId1, "", "DiaryProducts", "", 10, 7.5)); //productName cannot be empty
        assertThrows(ItemException.class, () -> service.addProductToStore(founderStore1Id, storeId1, "butter", "DiaryProducts", "", -1, 7.5)); //quantity cannot be < 0
        assertThrows(ItemException.class, () -> service.addProductToStore(founderStore1Id, storeId1, "butter", "DiaryProducts", "", 10, -1)); //price cannot be < 0

        assertThrows(NoPermissionException.class, () -> service.addProductToStore(founderStore2Id, storeId1, "butter", "DiaryProducts", "", 10, 7.5)); //founderStore2Id can't add in store1Id
        assertThrows(NoPermissionException.class, () -> service.addProductToStore(subs3Id, storeId1, "butter", "DiaryProducts", "", 10, 7.5)); //subs3Id can't add in store1Id
        assertThrows(NotLoggedInException.class, () -> service.addProductToStore(guest1Id, storeId1, "butter", "DiaryProducts", "", 10, 7.5)); //guest1Id can't add in store1Id
    }

    @Test
    void validDeleteProductFromStore() throws Exception{
        service.deleteProductFromStore(founderStore1Id, storeId1, productId1);
        assertTrue(service.getItemsByStore(founderStore1Id, storeId1).size() == 1); //started with 2 products ni store1, then deleted productId1
        service.deleteProductFromStore(founderStore1Id, storeId1, productId2);
        assertTrue(service.getItemsByStore(founderStore1Id, storeId1).size() == 0); //started with 2 products ni store1, then deleted them
    }

    @Test
    void deleteProductFromStoreNotExist() throws Exception{
        assertThrows(InvalidStoreIdException.class, () -> service.deleteProductFromStore(founderStore1Id, "100", productId1)); //"abc" is not a storeId
        assertThrows(ItemNotFoundException.class, () -> service.deleteProductFromStore(founderStore1Id, storeId1, "10")); //itemId 10 is not in storeId1

    }

    @Test
    void deleteProductFromStoreYouDontBelongTo() throws Exception{
        assertThrows(NoPermissionException.class, () -> service.deleteProductFromStore(founderStore1Id, storeId2, productId3));
        assertThrows(NotLoggedInException.class, () -> service.deleteProductFromStore(guest1Id, storeId1, productId1));
    }

    void updateProductDetailsUseCase() throws InvalidActionException {
        service.updateProductDetails(founderStore1Id, storeId1, productId1, null,25, null);
        service.updateProductDetails(founderStore1Id, storeId1, productId1, "newSub1",null, null);
        service.updateProductDetails(founderStore1Id, storeId1, productId1, null,null, 11.5);
    }
    @Test
    void validUpdateProductDetails() throws Exception{
        updateProductDetailsUseCase();
        assertTrue(service.getItems(null, null, null, "newSub1", null, null ,null, null).toString().contains("milk"));
        assertTrue(service.getItems(null, "milk", null, null, null, null ,null, null).toString().contains("11.5"));
    }

    @Test
    void updateNotExistProductDetails() throws Exception{
        assertThrows(ItemNotFoundException.class, () -> service.updateProductDetails(founderStore1Id, storeId1, "10", null,25, null)); //itemId 10 is not in storeId1
    }

    @Test
    void updateProductDetailsWithoutPermissions() throws Exception{
        assertThrows(NoPermissionException.class, () -> service.updateProductDetails(founderStore1Id, storeId2, productId3, null,25, null));
        assertThrows(NotLoggedInException.class, () -> service.updateProductDetails(guest1Id, storeId2, productId3, null,23, null));
        assertThrows(NoPermissionException.class, () -> service.updateProductDetails(subs3Id, storeId2, productId3, null,null, 7.0));
    }

    @Test
    void validAppointStoreOwner() throws Exception{
        service.appointStoreOwner(founderStore1Id, subs1UserName, storeId1);
        service.appointStoreOwner(founderStore2Id, store1Manager1UserName, storeId2);
        service.appointStoreOwner(founderStore2Id, store1FounderUserName, storeId2);

    }

    @Test
    void wrongAppointStoreOwner() throws Exception{
        assertThrows(NoPermissionException.class, () -> service.appointStoreOwner(founderStore1Id, subs1UserName, storeId2)); //founderStore1Id has no permissions at store2

        //test circular appoint:
        service.appointStoreOwner(founderStore1Id, subs1UserName, storeId1);
        assertThrows(AlreadyOwnerException.class, () -> service.appointStoreOwner(subs1Id, store1FounderUserName, storeId1));

        assertThrows(SubscriberDoesNotExistException.class, () -> service.appointStoreOwner(founderStore1Id, guest1UserName, storeId1)); //guest1Id is a guest
        assertThrows(SubscriberDoesNotExistException.class, () -> service.appointStoreOwner(founderStore1Id, "abc", storeId1)); //connection id not exist


    }

    @Test
    void validAllowManagerToUpdateProducts() throws Exception{
        service.allowManagerToUpdateProducts(founderStore1Id, storeId1, store1Manager1UserName);
        service.updateProductDetails(store1Manager1Id, storeId1, productId1, "newSubCateg", 2, null);
        assertTrue(service.getItems(null, null, null, "newSubCateg", null, null, null, null).toString().contains("milk"));
    }

    @Test
    void wrongAllowManagerToUpdateProducts() throws Exception{
        assertThrows(NoPermissionException.class, () -> service.allowManagerToUpdateProducts(founderStore1Id, storeId2, store1Manager1UserName)); //founderStore1Id doesn't have permissions in store2
        assertThrows(NoPermissionException.class, () -> service.allowManagerToUpdateProducts(founderStore1Id, storeId1, subs2UserName)); //subs2UserName is not a manager of store1
        assertThrows(SubscriberDoesNotExistException.class, () -> service.allowManagerToUpdateProducts(founderStore1Id, storeId1, guest1UserName)); //guest1UserName is not a manager of store1

        assertThrows(NoPermissionException.class, () -> service.allowManagerToUpdateProducts(founderStore2Id, storeId1, store1Manager1UserName)); //founderStore2Id is not a an owner of store1

    }

    @Test
    void disableManagerFromUpdateProducts() throws Exception{
        service.allowManagerToUpdateProducts(founderStore1Id, storeId1, store1Manager1UserName);
        service.updateProductDetails(store1Manager1Id, storeId1, productId1, "newSubCateg", 2, null);
        service.disableManagerFromUpdateProducts(founderStore1Id, storeId1, store1Manager1UserName);
        assertThrows(Exception.class, () -> service.updateProductDetails(store1Manager1Id, storeId1, productId1, null, 10, null));

    }

    @Test
    void disableManagerFromUpdateProductsWithoutPermissionsInStore() throws Exception{
        service.allowManagerToUpdateProducts(founderStore1Id, storeId1, store1Manager1UserName);
        assertThrows(NoPermissionException.class, () ->service.disableManagerFromUpdateProducts(founderStore2Id, storeId1, store1Manager1UserName));
        service.updateProductDetails(store1Manager1Id, storeId1, productId1, "newSubCateg", 2, null);

        //try to disable user that is not a manager in the store:
        assertThrows(SubscriberDoesNotExistException.class, () -> service.disableManagerFromUpdateProducts(founderStore1Id, storeId1, guest1UserName)); //guest1UserName in not a manager
        assertThrows(NoPermissionException.class, () -> service.disableManagerFromUpdateProducts(founderStore1Id, storeId1, subs1UserName)); //subs1Id in not a manager

    }

    @Test
    void validAllowManagerToEditPolicies() throws Exception{
        //TODO: when requirements of policies will be ready, expand this test.
        service.allowManagerToEditPolicies(founderStore1Id, storeId1, store1Manager1UserName);
    }

    @Test
    void wrongAllowManagerToEditPolicies() throws Exception{
        //TODO: when requirements of policies will be ready, expand this test.
//        assertThrows(Exception.class, () -> service.allowManagerToEditPolicies(founderStore1Id, storeId2, store1Manager1UserName)); //founderStore1Id doesn't have permissions in store2
//        assertThrows(Exception.class, () -> service.allowManagerToEditPolicies(founderStore1Id, storeId1, subs2UserName)); //subs2UserName is not a manager of store1
//        assertThrows(Exception.class, () -> service.allowManagerToEditPolicies(founderStore1Id, storeId1, guest1UserName)); //guest1UserName is not a manager of store1
//        assertThrows(Exception.class, () -> service.allowManagerToEditPolicies(founderStore2Id, storeId1, store1Manager1UserName)); //founderStore2Id is not a an owner of store1

    }

    @Test
    void disableManagerFromEditPolicies() throws Exception{
        //TODO: when requirements of policies will be ready, expand this test.
        service.allowManagerToEditPolicies(founderStore1Id, storeId1, store1Manager1UserName);
    }

    void addItemToBasketUseCase3() throws InvalidActionException {
        //2 purchases from store1:
        service.addItemToBasket(subs1Id, storeId1, productId1, 1);
        service.addItemToBasket(subs1Id, storeId1, productId2, 1);
        //1 purchase from store2:
        service.addItemToBasket(subs1Id, storeId2, productId3, 1);
    }

    @Test
    void validAllowManagerToGetHistory() throws InvalidActionException{
        addItemToBasketUseCase3();
        service.purchaseCart(subs1Id);
        assertThrows(NoPermissionException.class, () -> service.getSalesHistoryByStore(store1Manager1Id, storeId1)); //store1Manager1Id doesn't have permissions yet
        service.allowManagerToGetHistory(founderStore1Id, storeId1, store1Manager1UserName);
        assertTrue(service.getSalesHistoryByStore(store1Manager1Id, storeId1).size() == 1);
        assertTrue(service.getSalesHistoryByStore(store1Manager1Id, storeId1).toString().contains("milk"));
        assertTrue(service.getSalesHistoryByStore(store1Manager1Id, storeId1).toString().contains("cheese"));

    }


    @Test
    void wrongAllowManagerToGetHistory() throws InvalidActionException{
        addItemToBasketUseCase3();

        //make the purchases: 2 from store1 and 1 from store2.
        service.purchaseCart(subs1Id);

        //tests for assigning (allowing) managers without permissions:
        assertThrows(Exception.class, () -> service.allowManagerToGetHistory(founderStore1Id, storeId2, store1Manager1UserName)); //founderStore1Id doesn't have permissions in store2
        assertThrows(Exception.class, () -> service.allowManagerToGetHistory(founderStore1Id, storeId1, subs2UserName)); //subs2UserName is not a manager of store1
        assertThrows(Exception.class, () -> service.allowManagerToGetHistory(founderStore1Id, storeId1, guest1UserName)); //guest1UserName is not a manager of store1
        assertThrows(Exception.class, () -> service.allowManagerToGetHistory(founderStore2Id, storeId1, store1Manager1UserName)); //founderStore2Id is not an owner of store1

        //tests for checking that users didn't get permissions to get history by the previous wrong assigning:
        assertThrows(Exception.class, () -> service.getSalesHistoryByStore(store1Manager1Id, storeId2));
        assertThrows(Exception.class, () -> service.getSalesHistoryByStore(subs2Id, storeId1));
        assertThrows(Exception.class, () -> service.getSalesHistoryByStore(guest1Id, storeId1));
        assertThrows(Exception.class, () -> service.getSalesHistoryByStore(store1Manager1Id, storeId1));

    }


    @Test
    void disableManagerFromGetHistory() throws Exception{
        service.allowManagerToGetHistory(founderStore1Id, storeId1, store1Manager1UserName);
        service.getSalesHistoryByStore(store1Manager1Id, storeId1);
        service.disableManagerFromGetHistory(founderStore1Id, storeId1, store1Manager1UserName);
        assertThrows(Exception.class, () -> service.getSalesHistoryByStore(store1Manager1Id, storeId1));
    }

    @Test
    void disableManagerFromGetHistoryWithoutPermissionsInStore() throws Exception{
        service.allowManagerToGetHistory(founderStore1Id, storeId1, store1Manager1UserName);
        assertThrows(Exception.class, () ->service.disableManagerFromGetHistory(founderStore2Id, storeId1, store1Manager1UserName));
        service.getSalesHistoryByStore(store1Manager1Id, storeId1);

        //try to disable user that is not a manager in the store:
        assertThrows(Exception.class, () -> service.disableManagerFromGetHistory(founderStore1Id, storeId1, guest1UserName)); //guest1UserName in not a manager
        assertThrows(Exception.class, () -> service.disableManagerFromGetHistory(founderStore1Id, storeId1, subs1Id)); //subs1Id in not a manager

    }

    @Test
    void validRemoveManager() throws Exception{
        Collection<String> str = service.showStaffInfo(founderStore1Id, storeId1);
        assertTrue(str.size() == 2); //currently only 1 owner and 1 manager
        assertTrue(str.toString().contains(store1Manager1UserName));
        assertTrue(service.removeManager(founderStore1Id, storeId1, store1Manager1UserName) == true);
        str = service.showStaffInfo(founderStore1Id, storeId1);
        assertTrue(str.size() == 1);
        assertTrue(!str.toString().contains(store1Manager1UserName));
    }

    @Test
    void wrongRemoveManager() throws Exception{
        assertThrows(NoPermissionException.class, () -> service.removeManager(founderStore2Id, storeId1, store1Manager1UserName)); //founderStore2Id is not an owner of store1
//        assertThrows(Exception.class, () -> service.removeManager(founderStore1Id, storeId1, subs2UserName)); //subs2UserName is not a manager of store1
        assertThrows(SubscriberDoesNotExistException.class, () -> service.removeManager(founderStore1Id, storeId1, guest1UserName)); //guest1UserName is not a manager of store1

        //test for double removing: NOT REQUIRED
//        assertTrue(service.removeManager(founderStore1Id, storeId1, store1Manager1UserName) == true);
//        assertThrows(Exception.class, () -> service.removeManager(founderStore1Id, storeId1, store1Manager1UserName));

    }

    @Test
    void showStaffInfo() throws Exception{
        Collection<String> collection = service.showStaffInfo(founderStore1Id, storeId1);
        assertTrue(collection.size() == 2); //currently only 1 owner and 1 manager
        assertTrue(collection.toString().contains(store1FounderUserName));
        assertTrue(collection.toString().contains(store1Manager1UserName));

        collection = service.showStaffInfo(founderStore2Id, storeId2);
        assertTrue(collection.size() == 1); //currently only 1 owner
        assertTrue(collection.toString().contains(store2FounderUserName));
    }

    @Test
    void showStaffInfoStoreNotExist() throws Exception{
        assertThrows(InvalidStoreIdException.class, () -> service.showStaffInfo(founderStore2Id, "99"));

        assertThrows(InvalidStoreIdException.class, () -> service.showStaffInfo(admin1Id, "99"));
     }

    @Test
    void showStaffInfoNoPermissions() throws Exception{
        assertThrows(NoPermissionException.class, () -> service.showStaffInfo(founderStore1Id, storeId2));
        assertThrows(NoPermissionException.class, () -> service.showStaffInfo(store1Manager1Id, storeId2));
        assertThrows(NoPermissionException.class, () -> service.showStaffInfo(subs1Id, storeId1));
        assertThrows(NotLoggedInException.class, () -> service.showStaffInfo(guest1Id, storeId1));

    }

    @Test
    void getSalesHistoryByStore() throws Exception{
        /**user subs1Id purchases:*/
        //2 items from store1:
        service.addItemToBasket(subs1Id, storeId1, productId1, 1);
        service.addItemToBasket(subs1Id, storeId1, productId2, 1);
        //1 item from store2:
        service.addItemToBasket(subs1Id, storeId2, productId3, 1);
        //make the purchases: 2 from store1 and 1 from store2.
        service.purchaseCart(subs1Id);

        /**user subs2Id purchases:*/
        //1 item from store1:
        service.addItemToBasket(subs2Id, storeId1, productId2, 2);
        //1 item from store2:
        service.addItemToBasket(subs2Id, storeId2, productId4, 3);
        //make the purchases: 1 from store1 and 1 from store2.
        service.purchaseCart(subs2Id);

        /**test get sales history: */
        assertTrue(service.getSalesHistoryByStore(admin1Id, storeId1).size() == 2);
        assertTrue(service.getSalesHistoryByStore(founderStore1Id, storeId1).size() == 2);

        assertTrue(service.getSalesHistoryByStore(admin1Id, storeId2).size() == 2);
    }

    @Test
    void wrongGetSalesHistoryByStore() throws Exception {
        /**user subs1Id purchases:*/
        //2 items from store1:
        service.addItemToBasket(subs1Id, storeId1, productId1, 1);
        service.addItemToBasket(subs1Id, storeId1, productId2, 1);
        //1 item from store2:
        service.addItemToBasket(subs1Id, storeId2, productId3, 1);
        //make the purchases: 2 from store1 and 1 from store2.
        service.purchaseCart(subs1Id);

        assertThrows(NoPermissionException.class, () -> service.getSalesHistoryByStore(founderStore1Id, storeId2)); //founderStore1Id is not an owner of store2
        assertThrows(NoPermissionException.class, () -> service.getSalesHistoryByStore(subs2Id, storeId1)); //subs2UserName doesn't have permissions
        assertThrows(NotLoggedInException.class, () -> service.getSalesHistoryByStore(guest1Id, storeId1)); //guest1UserName doesn't have permissions
    }

    @Test
    void getEventLog() throws Exception{
        //events of adding items to basket
        service.addItemToBasket(subs1Id, storeId1, productId1, 1);
        service.addItemToBasket(subs1Id, storeId1, productId2, 1);
        //events of opening a store
        service.openNewStore(subs1Id, "store3");

        assertTrue(service.getEventLog(admin1Id).size() > 0);

        assertTrue(service.getEventLog(admin1Id).toString().contains("Register with userName: subs1UserName, password: *********"));

        assertTrue(service.getEventLog(admin1Id).toString().contains("User open new store named: store3"));
    }

    @Test
    void wrongGetEventLog() throws Exception{
        assertThrows(NoPermissionException.class, () -> service.getEventLog(founderStore1Id)); //founderStore1Id is only a store owner and not a system manager
    }

    @Test
    void passNotVisibleInLog() throws Exception {
        assertTrue(service.getEventLog(admin1Id).toString().contains(admin1UserName));
        assertTrue(service.getEventLog(admin1Id).toString().contains(subs1UserName));

        assertTrue(!service.getEventLog(admin1Id).toString().contains("1234"));
        assertTrue(!service.getEventLog(admin1Id).toString().contains("ad123"));

    }

    @Test
    void getErrorLog() throws Exception {
        //TODO: expand test after further implementation
//        try {
//            service.addItemToBasket(subs1Id, storeId1, productId1, 1000); //amount is more than actual amount in store
//        } catch (Exception e){}
//        try {
//            service.getStoresInfo(founderStore1Id); //founderStore1Id is only a store owner and not a system manager
//        } catch (Exception e){}
//
//        assertTrue(service.getErrorLog(admin1Id).size() > 0);
    }

    @Test
    void getErrorLogStoreOwner() throws Exception{
        //TODO: check what should be counted as error. expand test after further implementation
//        assertThrows(NoPermissionException.class, () -> service.getErrorLog(founderStore1Id)); //founderStore1Id is only a store owner and not a system manager
    }

    //TODO test spell checking

    @Test
    void spellCheckByKeyWordByName() throws Exception{
        //keyword by name
        assertEquals(2, service.getItems("milkk", null, null, null, null, null, null, null).size());
        assertTrue(service.getItems("milkk", null, null, null, null, null, null, null).toString().contains("milk"));

        assertEquals(2, service.getItems("mikl", null, null, null, null, null, null, null).size());
        assertTrue(service.getItems("mikl", null, null, null, null, null, null, null).toString().contains("milk"));

        assertEquals(1, service.getItems("cheesae", null, null, null, null, null, null, null).size());
        assertTrue(service.getItems("cheesae", null, null, null, null, null, null, null).toString().contains("cheese"));

        assertEquals(2, service.getItems("Milk", null, null, null, null, null, null, null).size());
        assertTrue(service.getItems("Milk", null, null, null, null, null, null, null).toString().contains("milk"));

    }

    @Test
    void spellCheckByKeyWordByCategory() throws Exception{
        //keyword by name
        assertEquals(3, service.getItems("DairyProduct", null, null, null, null, null, null, null).size());
        assertTrue(service.getItems("dairyproduct", null, null, null, null, null, null, null).toString().contains("milk"));

        assertEquals(1, service.getItems("braed", null, null, null, null, null, null, null).size());
        assertTrue(service.getItems("braed", null, null, null, null, null, null, null).toString().contains("baguette"));
    }

    @Test
    void spellCheckByProductName() throws Exception {
        Collection<String> items = service.getItems("", "milkk", null, null, null, null, null, null);
        assertEquals(items.size(), 2);
        assertTrue(items.toString().contains("milk"));

        items = service.getItems("", "mikl", null, null, null, null, null, null);
        assertEquals(items.size(), 2);
        assertTrue(items.toString().contains("milk"));

        items = service.getItems("", "Milk", null, null, null, null, null, null);
        assertEquals(2, items.size());
        assertTrue(items.toString().contains("milk"));

    }

    @Test
    void spellCheckByCategory() throws Exception {
        Collection<String> items = service.getItems("", "", "braed", null, null, null, null, null);
        assertTrue(items.size() == 1);
        assertTrue(items.toString().contains("baguette"));

        service.addProductToStore(founderStore2Id, storeId2, "tomato", "vegetable", "", 30, 6.5);

        items = service.getItems("", "", "vegetabl", null, null, null, null, null);
        assertTrue(items.size() == 1);
        assertTrue(items.toString().contains("tomato"));
    }


    //TODO add concurrency tests - stress test
    void testMultiplePurchases(){

    }
}