package service;

import authentication.UserAuthentication;
import exceptions.ConnectionIdDoesNotExistException;
import exceptions.LoginException;
import exceptions.NoPermissionException;
import exceptions.NotLoggedInException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Item;
import store.Store;
import tradingSystem.TradingSystem;
import user.Subscriber;
import user.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TradingSystemServiceImplTest {

    @Mock UserAuthentication auth;
    @Mock PaymentSystem paymentSystem;
    @Mock DeliverySystem deliverySystem;
    @Mock Map<String, Subscriber> subscribers;
    @Mock TradingSystem tradingSystem;
    @Mock Map<String, User> connections;
    @Mock Map<Integer, Store> stores;
    @Mock Subscriber subscriber;
    @Mock Store store;
    @Mock Item item1;
    @Mock Item item2;

    private final String userName = "Barak";
    private final String password = "1234";
    private final String connectionId = "4523532453245";
    private final String storeId = "43534532";

    TradingSystemServiceImpl service;

    @BeforeEach
    void setUp() throws LoginException {

        service = new TradingSystemServiceImpl(auth, paymentSystem, deliverySystem, subscribers, connections, stores);

        try (MockedStatic<TradingSystem> tradingSystemMockedStatic = Mockito.mockStatic(TradingSystem.class)) {
            tradingSystemMockedStatic.when(() -> TradingSystem.createTradingSystem(userName, password, paymentSystem,
                    deliverySystem, auth, subscribers, connections, stores)).thenReturn(tradingSystem);
            service.initializeSystem(userName, password);
        }
    }

    @Test
    void initializeSystem() {
    }

    @Test
    void connect() {
    }

    @Test
    void register() {
    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void getItems() {
    }

    @Test
    void addItemToBasket() {
    }

    @Test
    void showCart() {
    }

    @Test
    void showBasket() {
    }

    @Test
    void updateProductAmountInBasket() {
    }

    @Test
    void purchaseCart() {
    }

    @Test
    void getPurchaseHistory() {
    }

    @Test
    void writeOpinionOnProduct() {
    }

    @Test
    void getStoresInfo() {
    }

    @Test
    void getItemsByStore() throws NotLoggedInException, ConnectionIdDoesNotExistException, NoPermissionException {

        Collection<Item> items = new LinkedList<>();
        items.add(item1);
        items.add(item2);

        when(tradingSystem.getSubscriberByConnectionId(connectionId)).thenReturn(subscriber);
        when(tradingSystem.getStore(Integer.parseInt(storeId))).thenReturn(store);
        when(subscriber.getStoreItems(store)).thenReturn(items);
        when(item1.getName()).thenReturn("item1");
        when(item2.getName()).thenReturn("item2");

        Collection<String> result = service.getItemsByStore(connectionId, storeId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("item1"));
        assertTrue(result.contains("item2"));
    }

    @Test
    void openNewStore() {
    }

    @Test
    void appointStoreManager() {
    }

    @Test
    void addProductToStore() {
    }

    @Test
    void deleteProductFromStore() {
    }

    @Test
    void updateProductDetails() {
    }

    @Test
    void appointStoreOwner() {
    }

    @Test
    void allowManagerToUpdateProducts() {
    }

    @Test
    void disableManagerFromUpdateProducts() {
    }

    @Test
    void allowManagerToEditPolicies() {
    }

    @Test
    void disableManagerFromEditPolicies() {
    }

    @Test
    void allowManagerToGetHistory() {
    }

    @Test
    void disableManagerFromGetHistory() {
    }

    @Test
    void removeManager() {
    }

    @Test
    void showStaffInfo() {
    }

    @Test
    void getSalesHistoryByStore() {
    }

    @Test
    void getEventLog() {
    }

    @Test
    void getErrorLog() {
    }
}