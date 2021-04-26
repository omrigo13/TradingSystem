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
    private final Collection<simpleDiscountPolicy> discountPolicies = new ArrayList<>();
    private Collection<Item> items = new ArrayList<>();

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

    @AfterEach
    void tearDown() throws ItemException {
        store.removeItem(0);
        store.removeItem(1);
        policies.clear();
        discountPolicies.clear();
        items.clear();
    }

    @Test // 50% discount on cat1
    void discountByCategory() throws policyException, ItemException {
        Collection<Item> items = store.searchItems(null, null, "cat1");
        store.setDiscountPolicy(new quantityDiscountPolicy(50, items, null));
        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("33.0")); // checks that the purchase value correct
    }

    @Test // 20% discount on all store
    void discountByStore() throws policyException, ItemException {
        Collection<Item> items = store.getItems().keySet();
        store.setDiscountPolicy(new quantityDiscountPolicy(20, items, null));
        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("34.8")); // checks that the purchase value correct
    }

    @Test // 10% on cheese when basket value is more then 50
    void discountByBasketValueForItemWithGoodBasketValue() throws policyException, ItemException {
        Collection<Item> items = store.searchItems(null, "cheese", null);
        policies.add(new basketPurchasePolicy(50));
        store.setDiscountPolicy(new quantityDiscountPolicy(10, items, new andPolicy(policies))); //policy for 10% on cheese and basket value > 50

        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.getBasket(store).addItem(item2, 2);
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("50.4")); // checks that the purchase value correct
    }

    @Test // 10% on cheese when basket value is more then 50
    void discountByBasketValueForItemWithLowerBasketValue() throws policyException, ItemException {
        Collection<Item> items = store.searchItems(null, "cheese", null);
        policies.add(new basketPurchasePolicy(50));
        store.setDiscountPolicy(new quantityDiscountPolicy(10, items, new andPolicy(policies))); //policy for 10% on cheese and basket value > 50

        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("43.5")); // checks that the purchase value without 10% discount
    }

    @Test // and discount 5% on cheese and tomato if the basket contains at least 5 cheese and 2 tomatoes
    void andDiscountByBasketCondition() throws ItemException, policyException {
        items.add(item1);
        items.add(item2);
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        policies.add(new quantityPolicy(cheese, 5, 0));
        policies.add(new quantityPolicy(tomato, 2, 0));
        store.setDiscountPolicy(new quantityDiscountPolicy(5, items, new andPolicy(policies))); //policy for 5% on cheese and tomato

        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.getBasket(store).addItem(item1, 2);
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("54.625")); // checks that the purchase value correct
    }

    @Test // and discount 5% on cheese and tomato if the basket contains at least 5 cheese and 2 tomatoes
    void andDiscountByBasketConditionNotMet() throws ItemException, policyException {
        items.add(item1);
        items.add(item2);
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        policies.add(new quantityPolicy(cheese, 5, 0));
        policies.add(new quantityPolicy(tomato, 2, 0));
        store.setDiscountPolicy(new quantityDiscountPolicy(5, items, new andPolicy(policies))); //policy for 5% on cheese and tomato

        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("43.5")); // checks that the purchase value without 5% discount
    }

    @Test // or discount 5% on cheese if the basket contains at least 5 cheese or 7 tomatoes
    void orDiscountByBasketCondition() throws ItemException, policyException {
        items.add(item1);
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        policies.add(new quantityPolicy(cheese, 5, 0));
        policies.add(new quantityPolicy(tomato, 7, 0));
        store.setDiscountPolicy(new quantityDiscountPolicy(5, items, new orPolicy(policies))); //policy for 5% on cheese

        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.getBasket(store).addItem(item1, 2);
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("55.75")); // checks that the purchase value correct
    }

    @Test // or discount 5% on cheese if the basket contains at least 5 cheese or 7 tomatoes
    void orDiscountByBasketConditionNotMet() throws ItemException, policyException {
        items.add(item1);
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        policies.add(new quantityPolicy(cheese, 5, 0));
        policies.add(new quantityPolicy(tomato, 7, 0));
        store.setDiscountPolicy(new quantityDiscountPolicy(5, items, new orPolicy(policies))); //policy for 5% on cheese

        assertEquals(7.0, item1.getPrice());
        assertEquals(4.5, item2.getPrice());
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("43.5")); // checks that the purchase value without 5% discount
    }
}
