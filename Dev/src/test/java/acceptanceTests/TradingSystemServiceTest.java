package acceptanceTests;

import authentication.LoginException;
import authentication.UserAlreadyExistsException;
import authentication.UserDoesNotExistException;
import authentication.WrongPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TradingSystemService;

import static org.junit.jupiter.api.Assertions.*;

class TradingSystemServiceTest {
    private static TradingSystemService service;
    private String storeId1, storeId2; //stores
    private String productId1, productId2, productId3, productId4; //products
    private String admin1Id, founderStore1Id, founderStore2Id, store1Manager1Id, subs1Id, subs2Id, subs3Id, guest1Id; //users Id's
    //users names:
    private String admin1UserName="Admin1", store1FounderUserName="store1FounderUserName", store2FounderUserName="store2FounderUserName",
            store1Manager1UserName="Store1Manager1UserName", subs1UserName = "subs1UserName", subs2UserName = "subs2UserName",
            subs3UserName = "subs3UserName", guest1UserName = "guest1UserName";


    @BeforeEach
    public void setUp() throws Exception {
        service = Driver.getService();
        service.initializeSystem("Admin1", "ad123");
        admin1Id = service.connectGuest();
        founderStore1Id = service.connectGuest();
        founderStore2Id = service.connectGuest();
        store1Manager1Id = service.connectGuest();
        subs1Id = service.connectGuest();
        subs2Id = service.connectGuest();
        subs3Id = service.connectGuest();
        guest1Id = service.connectGuest();


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

        storeId2 = service.openNewStore(founderStore2Id, "store1");
        productId3 = service.addProductToStore(founderStore2Id, storeId1, "milk", "DairyProducts", "sub1", 30, 6.5);
        productId4 = service.addProductToStore(founderStore2Id, storeId1, "baguette", "bread", "", 20, 9);

        service.appointStoreManager(founderStore1Id, store1Manager1Id, storeId1);



    }

    @Test
    void initializeSystemWithGoodUserDetails() throws Exception {
        assertDoesNotThrow(() -> service.initializeSystem("OzMadmoni", "abc12345"));
    }

    @Test
    void initializeSystemWithWrongUserDetails() throws Exception {
        assertThrows(Exception.class, () ->service.initializeSystem("OzMadmoni", ""));
        assertThrows(Exception.class, () ->service.initializeSystem("", "abc12345"));

    }

    @Test
    void connectGuest() throws Exception{
        String s = "";
        s = service.connectGuest();
        assertTrue(s!=null && s.length() > 0);
        String s2 = "";
        s2 = service.connectGuest();
        assertTrue(s2!= null && s2.length() > 0);
        assertTrue(!(s.equals(s2)));
    }

    @Test
    void registerGoodDetails() throws Exception{
        assertDoesNotThrow(() -> service.register("LidorRubi", "123456"));
        assertDoesNotThrow(() -> service.register("asdasd", "123456"));
    }

    @Test
    void registerWrongDetails() throws Exception{
        assertThrows(Exception.class, () -> service.register("", "123456"));
        assertThrows(Exception.class, () -> service.register("asdasd", ""));
    }

    @Test
    void registerUserAlreadyExist() throws Exception{
        service.register("AAA",  "123");
        assertThrows(UserAlreadyExistsException.class, () -> service.register("AAA",  "123"));
    }

    @Test
    void validlogin() throws Exception{
        String id1 = service.connectGuest();
        String id2 = service.connectGuest();
        service.register("tempUser1", "1234");
        service.register("tempUser2", "1234");

        assertDoesNotThrow(() -> service.login(id1, "tempUser1", "1234"));
        assertDoesNotThrow(() -> service.login(id2, "tempUser2", "1234"));
    }

    @Test
    void alreadyLoggedIn() throws Exception{
        String id1 = service.connectGuest();
        String id2 = service.connectGuest();
        service.register("tempUser1", "1234");

        assertDoesNotThrow(() -> service.login(id1, "tempUser1", "1234"));
        assertThrows(LoginException.class, () -> service.login(id2, "tempUser1", "1234"));
    }

    @Test
    void userNotExistLogin() throws Exception{
        String id1 = service.connectGuest();
        String id2 = service.connectGuest();
        assertThrows(UserDoesNotExistException.class, () -> service.login(id2, "user999", "1234"));
    }

    @Test
    void wrongPasswordLogin() throws Exception{
        String id1 = service.connectGuest();
        String id2 = service.connectGuest();
        service.register("tempUser1", "1234");

        assertThrows(WrongPasswordException.class, () -> service.login(id2, "tempUser1", "1"));
    }


    @Test
    void validLogout() throws Exception{
        assertDoesNotThrow(() -> service.logout("Admin1"));
    }

    @Test
    void userNotExistLogout() throws Exception{
        assertThrows(UserDoesNotExistException.class, () -> service.logout("user999"));
    }

    @Test
    void alreadyLoggedOut() throws Exception{
        service.logout("Admin1");
        assertThrows(Exception.class, () -> service.logout("Admin1"));
    }

    @Test
    void getItemsByKeyWord() throws Exception{
        assertTrue(!service.getItems("milk", null, null, null, null, null, null, null).isEmpty());
    }

    @Test
    void getItemsByProductName() throws Exception {
        assertTrue(!service.getItems("", "milk", null, null, null, null, null, null).isEmpty());
        assertTrue(!service.getItems("", "baguette", null, null, null, null, null, null).isEmpty());
    }

    @Test
    void getItemsByCategory() throws Exception {
        assertTrue(!service.getItems("", "", "bread", null, null, null, null, null).isEmpty());
    }

    @Test
    void getItemsByPrice() throws Exception {
        assertTrue(!service.getItems("", "", "", null, null, null, 1000.0, 0.5).isEmpty());
    }

    @Test
    void validAddItemToBasket() throws Exception{
        assertDoesNotThrow(() -> service.addItemToBasket(store1Manager1Id, storeId1, productId1, 2));
    }

    void notValidAddItemToBasket() throws Exception{
        assertThrows(Exception.class, () -> service.addItemToBasket(store1Manager1Id, storeId1, productId1, 200));
        assertThrows(Exception.class, () -> service.addItemToBasket(store1Manager1Id, storeId2, productId1, 2));
        assertThrows(Exception.class, () -> service.addItemToBasket(store1Manager1Id, "asd", productId1, 2));
        assertThrows(Exception.class, () -> service.addItemToBasket(store1Manager1Id, storeId1, "asd", 2));
    }

    @Test
    void showCart() throws Exception{
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        assertTrue(service.showCart(store1Manager1Id).size() == 3);
        assertFalse(service.showCart(subs1Id).isEmpty());
    }

    @Test
    void showBasket() throws Exception{
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        String s1 = service.showBasket(store1Manager1Id,storeId1);
        assertTrue(s1 != null && !s1.isEmpty());
        String s2 = service.showBasket(store1Manager1Id,storeId2);
        assertTrue(s2 != null && !s2.isEmpty());
        assertThrows(Exception.class, () -> service.showBasket(store1Manager1Id,storeId2));
    }

    @Test
    void updateProductAmountInBasket() throws Exception{
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        String s1 = service.showBasket(store1Manager1Id,storeId1);
        assertTrue(s1 != null && !s1.isEmpty() && s1.contains("milk"));
        service.updateProductAmountInBasket(store1Manager1Id, storeId1, productId1, 0);
        s1 = service.showBasket(store1Manager1Id,storeId1);
        assertTrue(s1 != null && !s1.isEmpty() && !s1.contains("milk"));
        assertThrows(Exception.class, () -> service.updateProductAmountInBasket(store1Manager1Id, storeId2, productId3, 1000 ));  // bad amount
        assertThrows(Exception.class, () -> service.updateProductAmountInBasket(store1Manager1Id, storeId2, productId4, 1 ));    // productId4 not added by id4 to his basket
        assertThrows(Exception.class, () -> service.updateProductAmountInBasket(subs1Id, storeId2, productId4, 1 ));    // id5 didnt add nothing to his basket
        assertThrows(Exception.class, () -> service.updateProductAmountInBasket(store1Manager1Id, "abc", productId4, 1 ));  // abc store doesnt exist
    }

    @Test
    void purchaseCart() throws Exception{
        // TODO
    }

    @Test
    void getPurchaseHistory() throws Exception{
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        service.purchaseCart(store1Manager1Id);
        assertTrue(service.getPurchaseHistory(store1Manager1Id) != null && service.getPurchaseHistory(store1Manager1Id).size() == 3);
    }

    @Test
    void validWriteOpinionOnProduct() throws Exception{
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        service.purchaseCart(store1Manager1Id);
        assertDoesNotThrow(() -> service.writeOpinionOnProduct(store1Manager1Id, storeId1, productId1, "desc example1"));
        assertDoesNotThrow(() -> service.writeOpinionOnProduct(store1Manager1Id, storeId1, productId2, "desc example2"));
        assertDoesNotThrow(() -> service.writeOpinionOnProduct(store1Manager1Id, storeId2, productId3, "desc example3"));

    }

    @Test
    void writeOpinionOnProductNotPurchased() throws Exception {
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        service.purchaseCart(store1Manager1Id);
        assertThrows(Exception.class, () -> service.writeOpinionOnProduct(store1Manager1Id, storeId2, productId4, "opinion1"));
    }

    @Test
    void writeOpinionOnProductNotExist() throws Exception {
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        service.purchaseCart(store1Manager1Id);
        assertThrows(Exception.class, () -> service.writeOpinionOnProduct(store1Manager1Id, storeId2, "notExistId", "opinion1")); //no such productId in storeId2
        assertThrows(Exception.class, () -> service.writeOpinionOnProduct(store1Manager1Id, "abc", productId1, "opinion1")); //no such storeId "abc"
    }

    @Test
    void writeOpinionOnProductWrongDesc() throws Exception {
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        service.purchaseCart(store1Manager1Id);
        assertThrows(Exception.class, () -> service.writeOpinionOnProduct(store1Manager1Id, storeId1, productId1, null)); //null opinion
        assertThrows(Exception.class, () -> service.writeOpinionOnProduct(store1Manager1Id, storeId1, productId2, "")); //empty opinion
    }

    @Test
    void getStoresInfo() throws Exception{
        assertTrue(service.getStoresInfo(admin1Id).size()>0); //there are 2 stores opened
    }

    @Test
    void getStoresInfoWithoutPermissions() throws Exception{
        //id4 is a store manager of storeId1.
        assertThrows(Exception.class, () -> service.getStoresInfo(founderStore1Id)); //id2 is only a store owner and not a system manager
        assertThrows(Exception.class, () -> service.getStoresInfo(store1Manager1Id)); //id4 is only a store manager and not a system manager
        assertThrows(Exception.class, () -> service.getStoresInfo(subs1Id)); //id5 is only a guest and not a system manager

    }

    @Test
    void validGetItemsByStore() throws Exception{
        //id2 is store founder of storeId1, id3 is store founder of storeId2. id1 is a system manager.
        //system manager invokes:
        assertTrue(service.getItemsByStore(admin1Id, storeId1).size() == 2);
        assertTrue(service.getItemsByStore(admin1Id, storeId2).size() == 2);
        //store owner invokes:
        assertTrue(service.getItemsByStore(founderStore1Id, storeId1).size() == 2);
        assertTrue(service.getItemsByStore(founderStore2Id, storeId2).size() == 2);
    }

    @Test
    void wrongGetItemsByStore() throws Exception{
        //invokers without permissions
        assertThrows(Exception.class, () -> service.getItemsByStore(founderStore1Id, storeId2));
        assertThrows(Exception.class, () -> service.getItemsByStore(store1Manager1Id, storeId2));
        assertThrows(Exception.class, () -> service.getItemsByStore(subs1Id, storeId2));

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
        assertThrows(Exception.class, () -> service.openNewStore(guest1Id, "newStore20"));
    }

    @Test
    void openNewStoreWithWrongName() throws Exception{
        assertThrows(Exception.class, () -> service.openNewStore(founderStore1Id, null)); //null store name
        assertThrows(Exception.class, () -> service.openNewStore(founderStore1Id, "")); //empty store name
    }

    @Test
    void validAppointStoreManager() throws Exception{
        assertDoesNotThrow(() -> service.appointStoreManager(founderStore1Id, founderStore2Id, storeId1));
        assertDoesNotThrow(() -> service.appointStoreManager(founderStore1Id, subs1Id, storeId1));
        assertDoesNotThrow(() -> service.appointStoreManager(founderStore2Id, subs1Id, storeId2));
    }

    @Test
    void appointGuestAsStoreManager() throws Exception{
        assertThrows(Exception.class, () -> service.appointStoreManager(founderStore1Id, guest1Id, storeId1));
    }

    @Test
    void appointAnAlreadyStoreManager() throws Exception{
        assertThrows(Exception.class, () -> service.appointStoreManager(founderStore1Id, store1Manager1Id, storeId1));
        //test circular appoint:
        assertThrows(Exception.class, () -> service.appointStoreManager(store1Manager1Id, founderStore1Id, storeId1));

    }

    @Test
    void wrongAppointStoreManager() throws Exception{
        assertThrows(Exception.class, () -> service.appointStoreManager(founderStore2Id, subs1Id, storeId1)); //founderStore2Id is not an owner at storeId1
        assertThrows(Exception.class, () -> service.appointStoreManager(store1Manager1Id, subs2Id, storeId1)); //store1Manager1Id is not an owner at storeId1

    }

    @Test
    void validAddProductToStore() throws Exception{
        String prod1 = service.addProductToStore(founderStore1Id, storeId1, "butter", "DiaryProducts", "", 10, 7.5);
        assertTrue(prod1 != null && !prod1.isEmpty());
    }

    @Test
    void wrongAddProductToStore() throws Exception{
        assertThrows(Exception.class, () -> service.addProductToStore(founderStore1Id, "abc", "butter", "DiaryProducts", "", 10, 7.5)); //"abc" is not a storeId
        assertThrows(Exception.class, () -> service.addProductToStore(founderStore1Id, storeId1, "", "DiaryProducts", "", 10, 7.5)); //productName cannot be empty
        assertThrows(Exception.class, () -> service.addProductToStore(founderStore1Id, storeId1, "butter", "DiaryProducts", "", -1, 7.5)); //quantity cannot be < 0
        assertThrows(Exception.class, () -> service.addProductToStore(founderStore1Id, storeId1, "butter", "DiaryProducts", "", 10, -1)); //price cannot be < 0

        assertThrows(Exception.class, () -> service.addProductToStore(founderStore2Id, storeId1, "butter", "DiaryProducts", "", 10, 7.5)); //founderStore2Id can't add in store1Id
        assertThrows(Exception.class, () -> service.addProductToStore(subs3Id, storeId1, "butter", "DiaryProducts", "", 10, 7.5)); //subs3Id can't add in store1Id
        assertThrows(Exception.class, () -> service.addProductToStore(guest1Id, storeId1, "butter", "DiaryProducts", "", 10, 7.5)); //guest1Id can't add in store1Id

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
        assertThrows(Exception.class, () -> service.deleteProductFromStore(founderStore1Id, "abc", productId1)); //"abc" is not a storeId
        assertThrows(Exception.class, () -> service.deleteProductFromStore(founderStore1Id, storeId1, productId3)); //productId3 is not in storeId1

    }

    @Test
    void deleteProductFromStoreYouDontBelongTo() throws Exception{
        assertThrows(Exception.class, () -> service.deleteProductFromStore(founderStore1Id, storeId2, productId3));
        assertThrows(Exception.class, () -> service.deleteProductFromStore(guest1Id, storeId1, productId1));

    }

    @Test
    void validUpdateProductDetails() throws Exception{
        assertDoesNotThrow(() -> service.updateProductDetails(founderStore1Id, storeId1, productId1, null,25, null));
        assertDoesNotThrow(() -> service.updateProductDetails(founderStore1Id, storeId1, productId1, "newSub1",null, null));
        assertDoesNotThrow(() -> service.updateProductDetails(founderStore1Id, storeId1, productId1, null,null, 11.5));
    }

    @Test
    void updateNotExistProductDetails() throws Exception{
        assertThrows(Exception.class, () -> service.updateProductDetails(founderStore1Id, storeId1, productId3, null,25, null)); //productId3 not in storeId1
    }

    @Test
    void updateProductDetailsWithoutPermissions() throws Exception{
        assertThrows(Exception.class, () -> service.updateProductDetails(founderStore1Id, storeId2, productId3, null,25, null));
        assertThrows(Exception.class, () -> service.updateProductDetails(guest1Id, storeId2, productId3, null,23, null));
        assertThrows(Exception.class, () -> service.updateProductDetails(subs3Id, storeId2, productId3, null,null, 7.0));
    }

    @Test
    void validAppointStoreOwner() throws Exception{
        assertDoesNotThrow(() -> service.appointStoreOwner(founderStore1Id, subs1Id, storeId1));
        assertDoesNotThrow(() -> service.appointStoreOwner(founderStore1Id, store1Manager1Id, storeId1));
        assertDoesNotThrow(() -> service.appointStoreOwner(founderStore2Id, store1Manager1Id, storeId2));
        assertDoesNotThrow(() -> service.appointStoreOwner(founderStore2Id, founderStore1Id, storeId2));

    }

    @Test
    void wrongAppointStoreOwner() throws Exception{
        assertThrows(Exception.class, () -> service.appointStoreOwner(founderStore1Id, subs1Id, storeId2)); //founderStore1Id has no permissions at store2

        //test circular appoint:
        service.appointStoreOwner(founderStore1Id, subs1Id, storeId1);
        assertThrows(Exception.class, () -> service.appointStoreOwner(subs1Id, founderStore1Id, storeId1));

        assertThrows(Exception.class, () -> service.appointStoreOwner(founderStore1Id, guest1Id, storeId1)); //guest1Id is a guest

    }

    @Test
    void validAllowManagerToUpdateProducts() throws Exception{
        assertDoesNotThrow(() -> service.allowManagerToUpdateProducts(founderStore1Id, storeId1, store1Manager1UserName));
        assertDoesNotThrow(() -> service.updateProductDetails(store1Manager1Id, storeId1, productId1, "newSubCateg", 2, null));

    }

    @Test
    void wrongAllowManagerToUpdateProducts() throws Exception{
        assertThrows(Exception.class, () -> service.allowManagerToUpdateProducts(founderStore1Id, storeId2, store1Manager1UserName)); //founderStore1Id doesn't have permissions in store2
        assertThrows(Exception.class, () -> service.allowManagerToUpdateProducts(founderStore1Id, storeId1, subs2UserName)); //subs2UserName is not a manager of store1
        assertThrows(Exception.class, () -> service.allowManagerToUpdateProducts(founderStore1Id, storeId1, guest1UserName)); //guest1UserName is not a manager of store1

        assertThrows(Exception.class, () -> service.allowManagerToUpdateProducts(founderStore2Id, storeId1, store1Manager1UserName)); //founderStore2Id is not a an owner of store1

    }

    @Test
    void disableManagerFromUpdateProducts() throws Exception{
        service.allowManagerToUpdateProducts(founderStore1Id, storeId1, store1Manager1UserName);
        assertDoesNotThrow(() -> service.updateProductDetails(store1Manager1Id, storeId1, productId1, "newSubCateg", 2, null));
        service.disableManagerFromUpdateProducts(founderStore1Id, storeId1, store1Manager1UserName);
        assertThrows(Exception.class, () -> service.updateProductDetails(store1Manager1Id, storeId1, productId1, null, 10, null));

    }

    @Test
    void disableManagerFromUpdateProductsWithoutPermissionsInStore() throws Exception{
        assertDoesNotThrow(() -> service.allowManagerToUpdateProducts(founderStore1Id, storeId1, store1Manager1UserName));
        assertThrows(Exception.class, () ->service.disableManagerFromUpdateProducts(founderStore2Id, storeId1, store1Manager1UserName));
        assertDoesNotThrow(() -> service.updateProductDetails(store1Manager1Id, storeId1, productId1, "newSubCateg", 2, null));

        //try to disable user that is not a manager in the store:
        assertThrows(Exception.class, () -> service.disableManagerFromUpdateProducts(founderStore1Id, storeId1, guest1UserName)); //guest1UserName in not a manager
        assertThrows(Exception.class, () -> service.disableManagerFromUpdateProducts(founderStore1Id, storeId1, subs1Id)); //subs1Id in not a manager

    }

    @Test
    void allowManagerToEditPolicies() throws Exception{
    }

    @Test
    void disableManagerFromEditPolicies() throws Exception{
    }

    @Test
    void allowManagerToGetHistory() throws Exception{
    }

    @Test
    void disableManagerFromGetHistory() throws Exception{
    }

    @Test
    void removeManager() throws Exception{
    }

    @Test
    void showStaffInfo() throws Exception{
    }

    @Test
    void getSalesHistoryByStore() throws Exception{
    }

    @Test
    void getEventLog() throws Exception{
    }

    @Test
    void getErrorLog() throws Exception{
    }
}