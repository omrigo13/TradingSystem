package acceptanceTests;

import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TradingSystemService;

import java.util.Collection;
import java.util.LinkedList;

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

        service = Driver.getService("Admin1", "ad123"); //params are details of system manager to register into user authenticator
        service.initializeSystem("Admin1", "ad123"); //initialize system and login to system manager
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


//        service.login(admin1Id, "Admin1", "ad123"); //already logged in since initialization
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

    @Test
    void initializeSystemWithGoodUserDetails() throws Exception {
        assertDoesNotThrow(() -> service.initializeSystem("Admin1", "ad123"));
    }

    @Test
    void initializeSystemNotExistedUser() throws Exception {
        assertThrows(Exception.class, () ->service.initializeSystem("OzMadmoni", "abc"));
        assertThrows(Exception.class, () ->service.initializeSystem("", "abc12345"));

    }

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
    void registerSubscriberAlreadyExist() throws Exception{
        service.register("AAA",  "123");
        assertThrows(SubscriberAlreadyExistsException.class, () -> service.register("AAA",  "123"));
    }

    @Test
    void validlogin() throws Exception{
        String id1 = service.connect();
        String id2 = service.connect();
        service.register("tempUser1", "1234");
        service.register("tempUser2", "1234");

        assertDoesNotThrow(() -> service.login(id1, "tempUser1", "1234"));
        assertDoesNotThrow(() -> service.login(id2, "tempUser2", "1234"));
    }

    @Test
    void alreadyLoggedIn() throws Exception{
        String id1 = service.connect();
        String id2 = service.connect();
        service.register("tempUser1", "1234");

        assertDoesNotThrow(() -> service.login(id1, "tempUser1", "1234"));
        assertThrows(LoginException.class, () -> service.login(id2, "tempUser1", "1234"));
    }

    @Test
    void subscriberNotExistLogin() throws Exception{
        String id1 = service.connect();
        String id2 = service.connect();
        assertThrows(SubscriberDoesNotExistException.class, () -> service.login(id2, "user999", "1234"));
    }

    @Test
    void wrongPasswordLogin() throws Exception{
        String id1 = service.connect();
        String id2 = service.connect();
        service.register("tempUser1", "1234");

        assertThrows(WrongPasswordException.class, () -> service.login(id2, "tempUser1", "1"));
    }


    @Test
    void validLogout() throws Exception{
        assertDoesNotThrow(() -> service.logout(subs3Id));
    }

    @Test
    void userNotExistLogout() throws Exception{
        assertThrows(ConnectionIdDoesNotExistException.class, () -> service.logout("user999"));
    }

    @Test
    void alreadyLoggedOut() throws Exception{
        service.logout(subs3Id);
        assertThrows(Exception.class, () -> service.logout(subs3Id));
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
        Collection<String> collect = new LinkedList<>();
        collect = service.getItems("", "", "", null, null, null, 1000.0, 0.5);
        assertTrue(!collect.isEmpty());
    }

    @Test
    void validAddItemToBasket() throws Exception{
        assertDoesNotThrow(() -> service.addItemToBasket(store1Manager1Id, storeId1, productId1, 2));
    }

    @Test
    void notValidAddItemToBasket() throws Exception{
        //TODO we decided to check amount only at purchase and not at add to basket
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
    }

    @Test
    void showBasket() throws Exception{
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        Collection<String> s1 = service.showBasket(store1Manager1Id,storeId1);
        assertTrue(s1 != null && !s1.isEmpty());
        Collection<String> s2 = service.showBasket(store1Manager1Id,storeId2);
        assertTrue(s2 != null && !s2.isEmpty());
    }

    @Test
    void updateProductAmountInBasket() throws Exception{
        service.addItemToBasket(store1Manager1Id, storeId1, productId1, 1);
        service.addItemToBasket(store1Manager1Id, storeId1, productId2, 1);
        service.addItemToBasket(store1Manager1Id, storeId2, productId3, 1);
        Collection<String> s1 = service.showBasket(store1Manager1Id,storeId1);
        assertTrue(s1 != null && !s1.isEmpty() && s1.toString().contains("milk"));
        service.updateProductAmountInBasket(store1Manager1Id, storeId1, productId1, 0);
        s1 = service.showBasket(store1Manager1Id,storeId1);
        String ss1 = s1.toString();
        assertTrue(s1 != null && !s1.isEmpty() && s1.toString().contains("milk"));
        //TODO we decided to check amount only at purchase and not at add to basket
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
//        Collection<String> str = service.getPurchaseHistory(store1Manager1Id);
        //TODO test error, you did only one purchase so the size should be 1 and not 3
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
        //TODO we didnt checked null or emtry opinion, can be added
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
        assertDoesNotThrow(() -> service.appointStoreManager(founderStore1Id, store2FounderUserName, storeId1));
        assertDoesNotThrow(() -> service.appointStoreManager(founderStore1Id, subs1UserName, storeId1));
        assertDoesNotThrow(() -> service.appointStoreManager(founderStore2Id, subs1UserName, storeId2));
    }

    @Test
    void appointGuestAsStoreManager() throws Exception{
        assertThrows(Exception.class, () -> service.appointStoreManager(founderStore1Id, guest1UserName, storeId1));
    }

    @Test
    void appointAnAlreadyStoreManager() throws Exception{
        assertThrows(Exception.class, () -> service.appointStoreManager(founderStore1Id, store1Manager1UserName, storeId1));
        //test circular appoint:
        assertThrows(Exception.class, () -> service.appointStoreManager(store1Manager1Id, store1Manager1UserName, storeId1));

    }

    @Test
    void wrongAppointStoreManager() throws Exception{
        assertThrows(Exception.class, () -> service.appointStoreManager(founderStore2Id, subs1UserName, storeId1)); //founderStore2Id is not an owner at storeId1
        assertThrows(Exception.class, () -> service.appointStoreManager(store1Manager1Id, subs2UserName, storeId1)); //store1Manager1Id is not an owner at storeId1

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
        assertDoesNotThrow(() -> service.appointStoreOwner(founderStore1Id, subs1UserName, storeId1));
        assertDoesNotThrow(() -> service.appointStoreOwner(founderStore1Id, store1Manager1UserName, storeId1));
        assertDoesNotThrow(() -> service.appointStoreOwner(founderStore2Id, store1Manager1UserName, storeId2));
        assertDoesNotThrow(() -> service.appointStoreOwner(founderStore2Id, store1FounderUserName, storeId2));

    }

    @Test
    void wrongAppointStoreOwner() throws Exception{
        assertThrows(Exception.class, () -> service.appointStoreOwner(founderStore1Id, subs1UserName, storeId2)); //founderStore1Id has no permissions at store2

        //test circular appoint:
        service.appointStoreOwner(founderStore1Id, subs1UserName, storeId1);
        assertThrows(Exception.class, () -> service.appointStoreOwner(subs1Id, store1FounderUserName, storeId1));

        assertThrows(Exception.class, () -> service.appointStoreOwner(founderStore1Id, guest1UserName, storeId1)); //guest1Id is a guest

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
    void validAllowManagerToEditPolicies() throws Exception{
        assertDoesNotThrow(() -> service.allowManagerToEditPolicies(founderStore1Id, storeId1, store1Manager1UserName));
        //TODO: when requirements of policies will be ready, expand this test.
    }

    @Test
    void wrongAllowManagerToEditPolicies() throws Exception{
//        assertThrows(Exception.class, () -> service.allowManagerToEditPolicies(founderStore1Id, storeId2, store1Manager1UserName)); //founderStore1Id doesn't have permissions in store2
//        assertThrows(Exception.class, () -> service.allowManagerToEditPolicies(founderStore1Id, storeId1, subs2UserName)); //subs2UserName is not a manager of store1
//        assertThrows(Exception.class, () -> service.allowManagerToEditPolicies(founderStore1Id, storeId1, guest1UserName)); //guest1UserName is not a manager of store1
//        assertThrows(Exception.class, () -> service.allowManagerToEditPolicies(founderStore2Id, storeId1, store1Manager1UserName)); //founderStore2Id is not a an owner of store1
        //TODO: when requirements of policies will be ready, expand this test.

    }

    @Test
    void disableManagerFromEditPolicies() throws Exception{
        service.allowManagerToEditPolicies(founderStore1Id, storeId1, store1Manager1UserName);
        //TODO: when requirements of policies will be ready, expand this test.

    }

    @Test
    void validAllowManagerToGetHistory() throws Exception{
        //2 purchases from store1:
        service.addItemToBasket(subs1Id, storeId1, productId1, 1);
        service.addItemToBasket(subs1Id, storeId1, productId2, 1);
        //1 purchase from store2:
        service.addItemToBasket(subs1Id, storeId2, productId3, 1);
        assertThrows(Exception.class, () -> service.getSalesHistoryByStore(store1Manager1Id, storeId1)); //store1Manager1Id doesn't have permissions yet
        assertDoesNotThrow(() -> service.allowManagerToGetHistory(founderStore1Id, storeId1, store1Manager1UserName));
        assertTrue(service.getSalesHistoryByStore(store1Manager1Id, storeId1).size() == 2);
    }


    @Test
    void wrongAllowManagerToGetHistory() throws Exception{
        //2 items from store1:
        service.addItemToBasket(subs1Id, storeId1, productId1, 1);
        service.addItemToBasket(subs1Id, storeId1, productId2, 1);
        //1 items from store2:
        service.addItemToBasket(subs1Id, storeId2, productId3, 1);
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
        assertDoesNotThrow(() -> service.getSalesHistoryByStore(store1Manager1Id, storeId1));
        service.disableManagerFromGetHistory(founderStore1Id, storeId1, store1Manager1UserName);
        assertThrows(Exception.class, () -> service.getSalesHistoryByStore(store1Manager1Id, storeId1));

    }

    @Test
    void disableManagerFromGetHistoryWithoutPermissionsInStore() throws Exception{
        assertDoesNotThrow(() -> service.allowManagerToGetHistory(founderStore1Id, storeId1, store1Manager1UserName));
        assertThrows(Exception.class, () ->service.disableManagerFromGetHistory(founderStore2Id, storeId1, store1Manager1UserName));
        assertDoesNotThrow(() -> service.getSalesHistoryByStore(store1Manager1Id, storeId1));

        //try to disable user that is not a manager in the store:
        assertThrows(Exception.class, () -> service.disableManagerFromGetHistory(founderStore1Id, storeId1, guest1UserName)); //guest1UserName in not a manager
        assertThrows(Exception.class, () -> service.disableManagerFromGetHistory(founderStore1Id, storeId1, subs1Id)); //subs1Id in not a manager

    }

    @Test
    void validRemoveManager() throws Exception{
        assertTrue(service.showStaffInfo(founderStore1Id, storeId1).size() == 2); //currently only 1 owner and 1 manager
        Collection<String> str = service.showStaffInfo(founderStore1Id, storeId1);
        assertTrue(service.removeManager(founderStore1Id, storeId1, store1Manager1UserName) == true);
        assertTrue(service.showStaffInfo(founderStore1Id, storeId1).size() == 1);
        str = service.showStaffInfo(founderStore1Id, storeId1);
    }

    @Test
    void wrongRemoveManager() throws Exception{
        assertThrows(Exception.class, () -> service.removeManager(founderStore2Id, storeId1, store1Manager1UserName)); //founderStore2Id is not an owner of store1
        assertThrows(Exception.class, () -> service.removeManager(founderStore1Id, storeId1, subs2UserName)); //subs2UserName is not a manager of store1
        assertThrows(Exception.class, () -> service.removeManager(founderStore1Id, storeId1, guest1UserName)); //guest1UserName is not a manager of store1

        //test for double removing:
        assertTrue(service.removeManager(founderStore1Id, storeId1, store1Manager1UserName) == true);
        assertThrows(Exception.class, () -> service.removeManager(founderStore1Id, storeId1, store1Manager1UserName));

    }

    @Test
    void showStaffInfo() throws Exception{
        assertTrue(service.showStaffInfo(founderStore1Id, storeId1).size() == 2); //currently only 1 owner and 1 manager
        assertTrue(service.showStaffInfo(founderStore2Id, storeId2).size() == 1); //currently only 1 owner

    }

    @Test
    void showStaffInfoStoreNotExist() throws Exception{
        assertThrows(Exception.class, () -> service.showStaffInfo(admin1Id, "storeIdNotExist"));
     }

    @Test
    void showStaffInfoNoPermissions() throws Exception{
        assertThrows(Exception.class, () -> service.showStaffInfo(founderStore1Id, storeId2));
        assertThrows(Exception.class, () -> service.showStaffInfo(store1Manager1Id, storeId2));
        assertThrows(Exception.class, () -> service.showStaffInfo(subs1Id, storeId1));
        assertThrows(Exception.class, () -> service.showStaffInfo(guest1Id, storeId1));

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
        //TODO there are only 2 purchases and not 3 purchases
        assertTrue(service.getSalesHistoryByStore(admin1Id, storeId1).size() == 3);
        assertTrue(service.getSalesHistoryByStore(founderStore1Id, storeId1).size() == 3);

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

        assertThrows(Exception.class, () -> service.getSalesHistoryByStore(founderStore1Id, storeId2)); //founderStore1Id is not an owner of store2
        assertThrows(Exception.class, () -> service.getSalesHistoryByStore(subs2UserName, storeId1)); //subs2UserName doesn't have permissions
        assertThrows(Exception.class, () -> service.getSalesHistoryByStore(guest1UserName, storeId1)); //guest1UserName doesn't have permissions
    }

    @Test
    void getEventLog() throws Exception{
        //events of adding items to basket
        service.addItemToBasket(subs1Id, storeId1, productId1, 1);
        service.addItemToBasket(subs1Id, storeId1, productId2, 1);
        //events of opening a store
        service.openNewStore(subs1Id, "store3");

        assertTrue(service.getEventLog(admin1Id).size() > 0);
        //TODO: expand test after further implementation
    }

    @Test
    void wrongGetEventLog() throws Exception{
        assertThrows(Exception.class, () -> service.getEventLog(founderStore1Id)); //founderStore1Id is only a store owner and not a system manager
    }

    @Test
    void getErrorLog() throws Exception {
        try {
            service.addItemToBasket(subs1Id, storeId1, productId1, 1000); //amount is more than actual amount in store
        } catch (Exception e){}
        try {
            service.getStoresInfo(founderStore1Id); //founderStore1Id is only a store owner and not a system manager
        } catch (Exception e){}

        assertTrue(service.getErrorLog(admin1Id).size() > 0);
        //TODO: expand test after further implementation
    }

    @Test
    void wrongGetErrorLog() throws Exception{
        assertThrows(Exception.class, () -> service.getErrorLog(founderStore1Id)); //founderStore1Id is only a store owner and not a system manager
    }
}