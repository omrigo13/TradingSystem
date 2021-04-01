package acceptanceTests;

import authentication.LoginException;
import authentication.UserAlreadyExistsException;
import authentication.UserDoesNotExistException;
import authentication.WrongPasswordException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TradingSystemService;

import java.security.cert.CollectionCertStoreParameters;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class TradingSystemServiceTest {
    private static TradingSystemService service;
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


        String storeId1 = service.openNewStore(id2, "store of user3");
        service.addProductToStore(id2, storeId1, "milk", "DairyProducts", "sub1", 10, 6.5);
        service.addProductToStore(id2, storeId1, "cheese", "DairyProducts", "sub1", 20, 3);

        String storeId2 = service.openNewStore(id3, "store of user4");
        service.addProductToStore(id3, storeId1, "milk", "DairyProducts", "sub1", 30, 6.5);
        service.addProductToStore(id3, storeId1, "baguette", "bread", "", 20, 9);


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
    void getItems() throws Exception{
//list of logged in users to use:
//        service.login(id2, "user3", "1234");
//        service.login(id3, "user4", "1234");
//        service.login(id4, "user5", "1234");
//        service.login(id5, "user6", "1234");
//        service.login(id6, "user7", "1234");
//        service.login(id7, "user8", "1234");

        assertTrue(service.getItems("milk", null, null, null, null, null, 0, 0));
    }

    @Test
    void addItemToBasket() throws Exception{
    }

    @Test
    void showCart() throws Exception{
    }

    @Test
    void showBasket() throws Exception{
    }

    @Test
    void updateProductAmountInBasket() throws Exception{
    }

    @Test
    void purchaseCart() throws Exception{
    }

    @Test
    void getPurchaseHistory() throws Exception{
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