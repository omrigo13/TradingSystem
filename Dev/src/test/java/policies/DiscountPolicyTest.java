package policies;

import exceptions.ItemException;
import exceptions.PolicyException;
import externalServices.DeliverySystem;
import externalServices.PaymentSystem;
import notifications.Observable;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class DiscountPolicyTest {

    private User user;
    private final Collection<PurchasePolicy> policies = new ArrayList<>();
    private final Collection<DiscountPolicy> discountPolicies = new ArrayList<>();
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
        store.setObservable(new Observable());
        store.setPurchasePolicy(new DefaultPurchasePolicy());
        store.setDiscountPolicy(new DefaultDiscountPolicy(items));
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
    void discountByCategory() throws Exception {
        Collection<Item> items = store.searchItems(null, null, "cat1");
        store.setDiscountPolicy(new QuantityDiscountPolicy(50, items, null));
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("33.0")); // checks that the purchase value correct
    }

    @Test // 20% discount on all store
    void discountByStore() throws Exception {
        Collection<Item> items = store.getItems().keySet();
        store.setDiscountPolicy(new QuantityDiscountPolicy(20, items, null));
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("34.8")); // checks that the purchase value correct
    }

    @Test // 10% on cheese when basket value is more then 50
    void discountByBasketValueForItemWithGoodBasketValue() throws Exception {
        Collection<Item> items = store.searchItems(null, "cheese", null);
        policies.add(new BasketPurchasePolicy(50));
        store.setDiscountPolicy(new QuantityDiscountPolicy(10, items, new AndPolicy(policies))); //policy for 10% on cheese and basket value > 50

        user.getBasket(store).addItem(item2, 2);
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("50.4")); // checks that the purchase value correct
    }

    @Test // 10% on cheese when basket value is more then 50
    void discountByBasketValueForItemWithLowerBasketValue() throws Exception {
        Collection<Item> items = store.searchItems(null, "cheese", null);
        policies.add(new BasketPurchasePolicy(50));
        store.setDiscountPolicy(new QuantityDiscountPolicy(10, items, new AndPolicy(policies))); //policy for 10% on cheese and basket value > 50

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("43.5")); // checks that the purchase value without 10% discount
    }

    @Test // and discount 5% on cheese and tomato if the basket contains at least 5 cheese and 2 tomatoes
    void andDiscountByBasketCondition() throws Exception {
        items.add(item1);
        items.add(item2);
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        policies.add(new QuantityPolicy(cheese, 5, 0));
        policies.add(new QuantityPolicy(tomato, 2, 0));
        store.setDiscountPolicy(new QuantityDiscountPolicy(5, items, new AndPolicy(policies))); //policy for 5% on cheese and tomato

        user.getBasket(store).addItem(item1, 2);
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("54.625")); // checks that the purchase value correct
    }

    @Test // and discount 5% on cheese and tomato if the basket contains at least 5 cheese and 2 tomatoes
    void andDiscountByBasketConditionNotMet() throws Exception {
        items.add(item1);
        items.add(item2);
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        policies.add(new QuantityPolicy(cheese, 5, 0));
        policies.add(new QuantityPolicy(tomato, 2, 0));
        store.setDiscountPolicy(new QuantityDiscountPolicy(5, items, new AndPolicy(policies))); //policy for 5% on cheese and tomato

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("43.5")); // checks that the purchase value without 5% discount
    }

    @Test // or discount 5% on cheese if the basket contains at least 5 cheese or 7 tomatoes
    void orDiscountByBasketCondition() throws Exception {
        items.add(item1);
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        policies.add(new QuantityPolicy(cheese, 5, 0));
        policies.add(new QuantityPolicy(tomato, 7, 0));
        store.setDiscountPolicy(new QuantityDiscountPolicy(5, items, new OrPolicy(policies))); //policy for 5% on cheese

        user.getBasket(store).addItem(item1, 2);
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("55.75")); // checks that the purchase value correct
    }

    @Test // or discount 5% on cheese if the basket contains at least 5 cheese or 7 tomatoes
    void orDiscountByBasketConditionNotMet() throws Exception {
        items.add(item1);
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        policies.add(new QuantityPolicy(cheese, 5, 0));
        policies.add(new QuantityPolicy(tomato, 7, 0));
        store.setDiscountPolicy(new QuantityDiscountPolicy(5, items, new OrPolicy(policies))); //policy for 5% on cheese

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("43.5")); // checks that the purchase value without 5% discount
    }

    @Test // basket value > 50 and 5 cheese on basket so 5% on tomatoes
    void basketCompundCalculationDiscount() throws Exception {
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        policies.add(new QuantityPolicy(cheese, 5, 0));
        policies.add(new BasketPurchasePolicy(50));
        store.setDiscountPolicy(new QuantityDiscountPolicy(5, tomato, new AndPolicy(policies))); //policy for 5% on tomato, basket value > 50, at least 5 cheese

        user.getBasket(store).addItem(item1, 2);
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("56.375")); // checks that the purchase value correct
    }

    @Test // basket value > 50 and 5 cheese on basket so 5% on tomatoes
    void basketCompundCalculationDiscountCondiionNotMet() throws Exception {
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        policies.add(new QuantityPolicy(cheese, 5, 0));
        policies.add(new BasketPurchasePolicy(50));
        store.setDiscountPolicy(new QuantityDiscountPolicy(5, tomato, new AndPolicy(policies))); //policy for 5% on tomato, basket value > 50, at least 5 cheese

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("43.5")); // checks that the purchase value correct
    }

    @Test // discount on tomatoes in basket or cheese depends on best price basket for user
    void maxbasketDiscount() throws Exception {
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        discountPolicies.add(new QuantityDiscountPolicy(5, cheese, null));
        discountPolicies.add(new QuantityDiscountPolicy(10, tomato, null));
        store.setDiscountPolicy(new MaxDiscountPolicy(discountPolicies)); //policy for 10% on tomato or 5% on cheese

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("41.25")); // checks that the purchase value correct
    }

    @Test // discount on tomatoes in basket or cheese depends on best price basket for user
    void maxbasketDiscountOtherOption() throws Exception {
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        discountPolicies.add(new QuantityDiscountPolicy(10, cheese, null));
        discountPolicies.add(new QuantityDiscountPolicy(5, tomato, null));
        store.setDiscountPolicy(new MaxDiscountPolicy(discountPolicies)); //policy for 10% on tomato or 5% on cheese

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("41.4")); // checks that the purchase value correct
    }

    @Test // 5% discount on tomatoes and 20% discount on store (tomatoes discount = 25% and cheese discount = 20%)
    void plusbasketDiscount() throws Exception {
        Collection<Item> storeItems = store.getItems().keySet();
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        discountPolicies.add(new QuantityDiscountPolicy(5, tomato, null));
        discountPolicies.add(new QuantityDiscountPolicy(20, storeItems, null));
        store.setDiscountPolicy(new PlusDiscountPolicy(discountPolicies)); //policy for 25% on tomato and 20% on cheese

        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("33.675")); // checks that the purchase value correct
    }

    @Test // discount on tomatoes in basket or cheese depends on best price basket for user plus discount 20% on store
    void compoundPlusAndMaxDiscount() throws Exception {
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        Collection<Item> storeItems = store.getItems().keySet();
        Collection<DiscountPolicy> maxDiscountPolicies = new ArrayList<>();
        maxDiscountPolicies.add(new QuantityDiscountPolicy(5, cheese, null)); // discount 5% on cheese
        maxDiscountPolicies.add(new QuantityDiscountPolicy(10, tomato, null)); // discount 10% on tomato
        discountPolicies.add(new QuantityDiscountPolicy(20, storeItems, null)); // discount 20% on store
        discountPolicies.add(new MaxDiscountPolicy(maxDiscountPolicies));
        store.setDiscountPolicy(new PlusDiscountPolicy(discountPolicies)); //policy for 10% on tomato or 5% on cheese plus 20% on store
        //cheese costs 7.0 and got 3, tomato costs 4.5 and got 5
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("32.55")); // checks that the purchase value correct
    }

    @Test // discount on tomatoes in basket plus discount on store and max with discount on cheese
    void compoundMaxAndPlusDiscount() throws Exception {
        Collection<Item> cheese = store.searchItems(null, "cheese", null);
        Collection<Item> tomato = store.searchItems(null, "tomato", null);
        Collection<Item> storeItems = store.getItems().keySet();
        Collection<DiscountPolicy> plusDiscountPolicies = new ArrayList<>();
        plusDiscountPolicies.add(new QuantityDiscountPolicy(20, storeItems, null)); // discount 20% on store
        plusDiscountPolicies.add(new QuantityDiscountPolicy(10, tomato, null)); // discount 10% on tomato
        discountPolicies.add(new QuantityDiscountPolicy(70, cheese, null)); // discount 70% on cheese
        discountPolicies.add(new PlusDiscountPolicy(plusDiscountPolicies));
        store.setDiscountPolicy(new MaxDiscountPolicy(discountPolicies)); //policy for 10% on tomato and 20% store or 50% cheese
        //cheese costs 7.0 and got 3, tomato costs 4.5 and got 5
        user.purchaseCart(paymentSystem, deliverySystem);
        assertTrue(store.getPurchaseHistory().toString().contains("28.8")); // checks that the purchase value correct
    }
}
