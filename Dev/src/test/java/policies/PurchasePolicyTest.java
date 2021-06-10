package policies;

import exceptions.*;
import externalServices.DeliveryData;
import externalServices.DeliverySystem;
import externalServices.PaymentData;
import externalServices.PaymentSystem;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import store.Item;
import store.Store;
import user.User;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.spy;
import static org.testng.Assert.assertThrows;

public class PurchasePolicyTest {

    private User user;
    private final Collection<PurchasePolicy> policies = new ArrayList<>();

    @Mock private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;
    @Mock private PaymentData paymentData;
    @Mock private DeliveryData deliveryData;
    @Mock private DiscountPolicy discountPolicy;

    private Store store;
    private Item item1, item2;

    @BeforeMethod
    void setUp() throws ItemException {
        MockitoAnnotations.openMocks(this);
        store = spy(new Store());
        item1 = spy(new Item());
        item2 = spy(new Item());
        user = new User();
        user.makeCart(user);
        store.setPurchasePolicy(new DefaultPurchasePolicy());
        store.setDiscountPolicy(discountPolicy);
        store.addItem("cheese", 7.0, "cat1", "sub1", 5);
        store.addItem("tomato", 4.5, "cat2", "sub2", 12);
        item1 = store.searchItemById(0);
        item2 = store.searchItemById(1);
        user.getBasket(store).addItem(item1, 3);
        user.getBasket(store).addItem(item2, 5);
    }

    void updateDetails() {
        store.getItems().get(item1.getItem_id()).setAmount(5);
        store.getItems().get(item2.getItem_id()).setAmount(12);
    }
    @AfterMethod
    void tearDown() throws ItemException {
        store.removeItem(0);
        store.removeItem(1);
        policies.clear();
    }

    @Test //should be here {1,0} {0,1}
    void xorPolicyByItemGoodDetails() throws InvalidActionException {
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 12));
        policies.add(new QuantityPolicy(store.getItems().values(), 6, 12));
        store.setPurchasePolicy(new XorPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData);

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.getItems().values(), 6, 12));
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 12));
        store.setPurchasePolicy(new XorPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData);
    }

    @Test
    // //should be here {1,1} {0,0}
    void xorPolicyByItemBothPoliciesValidOrNotValid() throws PolicyException {
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 12));
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 12));
        store.setPurchasePolicy(new XorPolicy(policies));
        assertThrows(XorPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData));

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 3));
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 4));
        store.setPurchasePolicy(new XorPolicy(policies));
        assertThrows(XorPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData));
    }

    @Test //should be here {1,1} {0,1} {1,0}
    void orPolicyByCategoryGoodDetails() throws InvalidActionException {
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 0, 6));
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 6, 12));
        store.setPurchasePolicy(new OrPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData);

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 6, 12));
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 0, 6));
        store.setPurchasePolicy(new OrPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData);

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 0, 8));
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 4, 8));
        store.setPurchasePolicy(new OrPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData);
    }

    @Test //should be here {0,0}
    void orPolicyByCategoryBothPoliciesNotValid() throws PolicyException {
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 0, 3));
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 0, 4));
        store.setPurchasePolicy(new OrPolicy(policies));
        assertThrows(OrPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData));
    }

    @Test //should be here {1,1}
    void andPolicyByItemGoodDetails() throws InvalidActionException {
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 12));
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 12));
        store.setPurchasePolicy(new AndPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData);
    }

    @Test //should be here {0,1} {1,0} {0,0}
    void andPolicyByItemAtLeastOnePolicyNotValid() throws PolicyException {
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 12));
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 4));
        store.setPurchasePolicy(new AndPolicy(policies));
        assertThrows(AndPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData));

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 3));
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 12));
        store.setPurchasePolicy(new AndPolicy(policies));
        assertThrows(AndPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData));

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 3));
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 4));
        store.setPurchasePolicy(new AndPolicy(policies));
        assertThrows(AndPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData));
    }

    @Test
    void quantityPolicyMinMaxQuantityBelowZero() {
        assertThrows(QuantityPolicyException.class, ()->policies.add(new QuantityPolicy(store.getItems().values(), -1, 0)));
        assertThrows(QuantityPolicyException.class, ()->policies.add(new QuantityPolicy(store.getItems().values(), 0, -1)));
    }

    @Test
    void quantityPolicyMinBiggerThenMax() {
        assertThrows(QuantityPolicyException.class, ()->policies.add(new QuantityPolicy(store.getItems().values(), 6, 3)));
    }

    @Test
    void quantityPolicyForItemDoesntExist() throws ItemException, PolicyException {
        store.addItem("banana", 9.5, "cat2", "sub2", 7);
        Item item3 = store.searchItemById(2);
        policies.add(new QuantityPolicy(store.getItems().values(), 0, 12));
        store.setPurchasePolicy(new AndPolicy(policies));
        assertThrows(QuantityPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData));
    }

    @Test
    void quantityPolicyBasketWrongMinQauntityItem() throws PolicyException {
        user.getBasket(store).setQuantity(item1, 1);
        store.setPurchasePolicy(new QuantityPolicy(store.getItems().values(), 2, 4));
        assertThrows(PolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData));
    }

    @Test
    void quantityPolicyBasketWrongMaxQuantityItem() throws PolicyException {
        user.getBasket(store).setQuantity(item1, 5);
        user.getBasket(store).setQuantity(item2, 4);
        store.setPurchasePolicy(new QuantityPolicy(store.getItems().values(), 2, 4));
        assertThrows(PolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData));
    }

    @Test
    void timePolicyGoodDetails() throws InvalidActionException {
        user.getBasket(store).setQuantity(item1, 5);
        user.getBasket(store).setQuantity(item2, 4);
        store.setPurchasePolicy(new TimePolicy(store.getItems().values(), LocalTime.of(0,0)));
        user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData);
    }

    @Test
    void timePolicyBadDetails() {
        user.getBasket(store).setQuantity(item1, 5);
        user.getBasket(store).setQuantity(item2, 4);
        store.setPurchasePolicy(new TimePolicy(store.getItems().values(), LocalTime.of(23,59)));
        assertThrows(PolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem, paymentData, deliveryData));
    }
}
