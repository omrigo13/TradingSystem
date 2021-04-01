package acceptanceTests;

import authentication.LoginException;
import authentication.UserAlreadyExistsException;
import authentication.UserDoesNotExistException;
import authentication.WrongPasswordException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import service.TradingSystemService;

import java.security.cert.CollectionCertStoreParameters;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class TradingSystemServiceTest {
    private static TradingSystemService service;
    private String storeId1, storeId2;
    private String productId1, productId2, productId3, productId4;
    private String id1, id2, id3, id4, id5, id6, id7, id8;

    @BeforeEach
    public void setUp() throws Exception {
        service = Driver.getService();
        service.initializeSystem("Admin1", "ad123");
        id1 = service.connectGuest();
        id2 = service.connectGuest();
        id3 = service.connectGuest();
        id4 = service.connectGuest();
        id5 = service.connectGuest();
        id6 = service.connectGuest();
        id7 = service.connectGuest();
        id8 = service.connectGuest();


        service.register("user1", "1234");
        service.register("user2", "1234");
        service.register("user3", "1234");
        service.register("user4", "1234");

        service.login(id1, "Admin1", "ad123");

        service.register("user3", "1234");
        service.register("user4", "1234");
        service.register("user5", "1234");
        service.register("user6", "1234");
        service.register("user7", "1234");
        service.register("user8", "1234");

        service.login(id2, "user3", "1234");
        service.login(id3, "user4", "1234");
        service.login(id4, "user5", "1234");
        service.login(id5, "user6", "1234");
        service.login(id6, "user7", "1234");
        service.login(id7, "user8", "1234");


        storeId1 = service.openNewStore(id2, "store of user3");
        productId1 = service.addProductToStore(id2, storeId1, "milk", "DairyProducts", "sub1", 10, 6.5);
        productId2 = service.addProductToStore(id2, storeId1, "cheese", "DairyProducts", "sub1", 20, 3);

        storeId2 = service.openNewStore(id3, "store of user4");
        productId3 = service.addProductToStore(id3, storeId1, "milk", "DairyProducts", "sub1", 30, 6.5);
        productId4 = service.addProductToStore(id3, storeId1, "baguette", "bread", "", 20, 9);


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
        assertDoesNotThrow(() -> service.login(id1, "user1", "1234"));
        assertDoesNotThrow(() -> service.login(id2, "user2", "1234"));
    }

    @Test
    void alreadyLoggedIn() throws Exception{
        String id1 = service.connectGuest();
        String id2 = service.connectGuest();
        assertDoesNotThrow(() -> service.login(id1, "user1", "1234"));
        assertThrows(LoginException.class, () -> service.login(id2, "user1", "1234"));
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
        assertThrows(WrongPasswordException.class, () -> service.login(id2, "user1", "1"));
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
//list of logged in users to use:
//        service.login(id2, "user3", "1234");
//        service.login(id3, "user4", "1234");
//        service.login(id4, "user5", "1234");
//        service.login(id5, "user6", "1234");
//        service.login(id6, "user7", "1234");
//        service.login(id7, "user8", "1234");

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
        assertDoesNotThrow(() -> service.addItemToBasket(id4, storeId1, productId1, 2));
    }

    void notValidAddItemToBasket() throws Exception{
        assertThrows(Exception.class, () -> service.addItemToBasket(id4, storeId1, productId1, 200));
        assertThrows(Exception.class, () -> service.addItemToBasket(id4, storeId2, productId1, 2));
        assertThrows(Exception.class, () -> service.addItemToBasket(id4, "asd", productId1, 2));
        assertThrows(Exception.class, () -> service.addItemToBasket(id4, storeId1, "asd", 2));
    }

    @Test
    void showCart() throws Exception{
        service.addItemToBasket(id4, storeId1, productId1, 1);
        service.addItemToBasket(id4, storeId1, productId2, 1);
        service.addItemToBasket(id4, storeId2, productId3, 1);
        assertTrue(service.showCart(id4).size() == 3);
        assertFalse(service.showCart(id5).isEmpty());
    }

    @Test
    void showBasket() throws Exception{
        service.addItemToBasket(id4, storeId1, productId1, 1);
        service.addItemToBasket(id4, storeId1, productId2, 1);
        service.addItemToBasket(id4, storeId2, productId3, 1);
        String s1 = service.showBasket(id4,storeId1);
        assertTrue(s1 != null && !s1.isEmpty());
        String s2 = service.showBasket(id4,storeId2);
        assertTrue(s2 != null && !s2.isEmpty());
        assertThrows(Exception.class, () -> service.showBasket(id4,storeId2));
    }

    @Test
    void updateProductAmountInBasket() throws Exception{
        service.addItemToBasket(id4, storeId1, productId1, 1);
        service.addItemToBasket(id4, storeId1, productId2, 1);
        service.addItemToBasket(id4, storeId2, productId3, 1);
        String s1 = service.showBasket(id4,storeId1);
        assertTrue(s1 != null && !s1.isEmpty() && s1.contains("milk"));
        service.updateProductAmountInBasket(id4, storeId1, productId1, 0);
        s1 = service.showBasket(id4,storeId1);
        assertTrue(s1 != null && !s1.isEmpty() && !s1.contains("milk"));
        assertThrows(Exception.class, () -> service.updateProductAmountInBasket(id4, storeId2, productId3, 1000 ));  // bad amount
        assertThrows(Exception.class, () -> service.updateProductAmountInBasket(id4, storeId2, productId4, 1 ));    // productId4 not added by id4 to his basket
        assertThrows(Exception.class, () -> service.updateProductAmountInBasket(id5, storeId2, productId4, 1 ));    // id5 didnt add nothing to his basket
        assertThrows(Exception.class, () -> service.updateProductAmountInBasket(id4, "abc", productId4, 1 ));  // abc store doesnt exist
    }

    @Test
    void purchaseCart() throws Exception{
        // TODO
    }

    @Test
    void getPurchaseHistory() throws Exception{
        service.addItemToBasket(id4, storeId1, productId1, 1);
        service.addItemToBasket(id4, storeId1, productId2, 1);
        service.addItemToBasket(id4, storeId2, productId3, 1);
        service.purchaseCart(id4);
        assertTrue(service.getPurchaseHistory(id4) != null && service.getPurchaseHistory(id4).size() == 3);
    }

    @Test
    void writeOpinionOnProduct() throws Exception{
    }

    @Test
    void getStoresInfo() throws Exception{
    }

    @Test
    void getItemsByStore() throws Exception{
    }

    @Test
    void openNewStore() throws Exception{
    }

    @Test
    void appointStoreManager() throws Exception{
    }

    @Test
    void addProductToStore() throws Exception{
    }

    @Test
    void deleteProductFromStore() throws Exception{
    }

    @Test
    void updateProductDetails() throws Exception{
    }

    @Test
    void appointStoreOwner() throws Exception{
    }

    @Test
    void allowManagerToUpdateProducts() throws Exception{
    }

    @Test
    void disableManagerFromUpdateProducts() throws Exception{
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