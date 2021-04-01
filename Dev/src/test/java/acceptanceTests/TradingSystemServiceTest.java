package acceptanceTests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.TradingSystemService;

import java.security.cert.CollectionCertStoreParameters;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class TradingSystemServiceTest {
    private static TradingSystemService service;

    @BeforeEach
    public void setUp(){
        service = Driver.getService();
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
    String connectGuest() throws Exception{
    }

    @Test
    void register() throws Exception{
    }

    @Test
    void login() throws Exception{
    }

    @Test
    void logout() throws Exception{
    }

    @Test
    Collection<String> getItems() throws Exception{
    }

    @Test
    void addItemToBasket() throws Exception{
    }

    @Test
    Collection<String> showCart() throws Exception{
    }

    @Test
    String showBasket() throws Exception{
    }

    @Test
    void updateProductAmountInBasket() throws Exception{
    }

    @Test
    void purchaseCart() throws Exception{
    }

    @Test
    Collection<String> getPurchaseHistory() throws Exception{
    }

    @Test
    void writeOpinionOnProduct() throws Exception{
    }

    @Test
    Collection<String> getStoresInfo() throws Exception{
    }

    @Test
    Collection<String> getItemsByStore() throws Exception{
    }

    @Test
    String openNewStore() throws Exception{
    }

    @Test
    void appointStoreManager() throws Exception{
    }

    @Test
    String addProductToStore() throws Exception{
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
    Collection<String> showStaffInfo() throws Exception{
    }

    @Test
    Collection<String> getSalesHistoryByStore() throws Exception{
    }

    @Test
    Collection<String> getEventLog() throws Exception{
    }

    @Test
    Collection<String> getErrorLog() throws Exception{
    }
}