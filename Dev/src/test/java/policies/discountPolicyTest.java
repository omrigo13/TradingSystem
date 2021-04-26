package policies;

import exceptions.ItemException;
import exceptions.policyException;
import exceptions.quantityDiscountPolicyException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import store.Item;
import store.Store;
import user.User;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class discountPolicyTest {

    private User user;
    private final Collection<simplePurchasePolicy> policies = new ArrayList<>();

    @Mock
    private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;

    @Spy
    private Store store;
    @Spy private Item item1, item2;

    @BeforeEach
    void setUp() throws ItemException {
        user = new User();
        user.makeCart(user);
        store.setPurchasePolicy(new defaultPurchasePolicy());
        store.setDiscountPolicy(new defaultDiscountPolicy());
        store.addItem("cheese", 7.0, "cat1", "sub1", 5);
        store.addItem("tomato", 4.5, "cat2", "sub2", 12);
        item1 = store.searchItemById(0);
        item2 = store.searchItemById(1);
        user.getBasket(store).addItem(item1, 3);
        user.getBasket(store).addItem(item2, 5);
    }

    void updateDetails() {
        user.getBasket(store).addItem(item1, 3);
        user.getBasket(store).addItem(item2, 5);
        store.getItems().replace(item1, 5);
        store.getItems().replace(item2, 12);
    }

    @AfterEach
    void tearDown() throws ItemException {
        store.removeItem(0);
        store.removeItem(1);
        policies.clear();
    }

    @Test // 50% discount on cat1
    void discountByCategory() throws policyException, ItemException {
        Collection<Item> items = store.searchItems(null, null, "cat1");
        store.setDiscountPolicy(new quantityDiscountPolicy(50, items, null));
        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("33.0")); // checks that the purchase value correct
        assertEquals(3.5, item1.getPrice()); // cat1 items updated after store discount
    }

    @Test // 20% discount on all store
    void discountByStore() throws policyException, ItemException {
        Collection<Item> items = store.getItems().keySet();
        store.setDiscountPolicy(new quantityDiscountPolicy(20, items, null));
        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("34.8")); // checks that the purchase value correct
        assertEquals(5.6, Math.round(item1.getPrice() * 100.0) / 100.0); // cat1 items updated after store discount
        assertEquals(3.6, Math.round(item2.getPrice() * 100.0) / 100.0); // cat2 items updated after store discount
    }

    @Test // 10% on cheese when basket value is more then 50
    void discountByBasketValueForItem() throws policyException, ItemException {
        Collection<Item> items = store.searchItems(null, "cheese", null);
        policies.add(new basketPurchasePolicy(50));
        store.setDiscountPolicy(new quantityDiscountPolicy(10, items, new andPolicy(policies))); //policy for 10% on cheese and basket value > 50

        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("43.5")); // checks that the purchase value without 10% discount
        assertEquals(7.0, Math.round(item1.getPrice() * 100.0) / 100.0); // cat1 items stay the same
        assertEquals(4.5, Math.round(item2.getPrice() * 100.0) / 100.0); // cat2 items stay the same

        updateDetails();
        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.getBasket(store).addItem(item2, 2);
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("50.4")); // checks that the purchase value correct
        assertEquals(6.3, Math.round(item1.getPrice() * 100.0) / 100.0); // cheese price updated with discount of 10%
        assertEquals(4.5, Math.round(item2.getPrice() * 100.0) / 100.0); // cat2 items stay the same
    }

}
