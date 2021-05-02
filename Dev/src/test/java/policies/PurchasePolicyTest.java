package policies;

import exceptions.*;
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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PurchasePolicyTest {

    private User user;
    private final Collection<PurchasePolicy> policies = new ArrayList<>();

    @Mock private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;
    @Mock private DiscountPolicy discountPolicy;

    @Spy private Store store;
    @Spy private Item item1, item2, item3;

    @BeforeEach
    void setUp() throws ItemException {
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
        store.getItems().replace(item1, 5);
        store.getItems().replace(item2, 12);
    }
    @AfterEach
    void tearDown() throws ItemException {
        store.removeItem(0);
        store.removeItem(1);
        policies.clear();
    }

    @Test //should be here {1,0} {0,1}
    void xorPolicyByItemGoodDetails() throws Exception {
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 12));
        policies.add(new QuantityPolicy(store.getItems().keySet(), 6, 12));
        store.setPurchasePolicy(new XorPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.getItems().keySet(), 6, 12));
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 12));
        store.setPurchasePolicy(new XorPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);
    }

    @Test
    // //should be here {1,1} {0,0}
    void xorPolicyByItemBothPoliciesValidOrNotValid() throws ItemException, PolicyException {
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 12));
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 12));
        store.setPurchasePolicy(new XorPolicy(policies));
        assertThrows(XorPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 3));
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 4));
        store.setPurchasePolicy(new XorPolicy(policies));
        assertThrows(XorPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test //should be here {1,1} {0,1} {1,0}
    void orPolicyByCategoryGoodDetails() throws Exception {
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 0, 6));
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 6, 12));
        store.setPurchasePolicy(new OrPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 6, 12));
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 0, 6));
        store.setPurchasePolicy(new OrPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 0, 8));
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 4, 8));
        store.setPurchasePolicy(new OrPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);
    }

    @Test //should be here {0,0}
    void orPolicyByCategoryBothPoliciesNotValid() throws ItemException, PolicyException {
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 0, 3));
        policies.add(new QuantityPolicy(store.searchItems(null, null, "cat2"), 0, 4));
        store.setPurchasePolicy(new OrPolicy(policies));
        assertThrows(OrPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test //should be here {1,1}
    void andPolicyByItemGoodDetails() throws Exception {
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 12));
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 12));
        store.setPurchasePolicy(new AndPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);
    }

    @Test //should be here {0,1} {1,0} {0,0}
    void andPolicyByItemAtLeastOnePolicyNotValid() throws ItemException, PolicyException {
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 12));
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 4));
        store.setPurchasePolicy(new AndPolicy(policies));
        assertThrows(AndPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 3));
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 12));
        store.setPurchasePolicy(new AndPolicy(policies));
        assertThrows(AndPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));

        updateDetails();
        policies.clear();
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 3));
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 4));
        store.setPurchasePolicy(new AndPolicy(policies));
        assertThrows(AndPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test
    void quantityPolicyMinMaxQuantityBelowZero() throws ItemException, PolicyException {
        assertThrows(QuantityPolicyException.class, ()->policies.add(new QuantityPolicy(store.getItems().keySet(), -1, 0)));
        assertThrows(QuantityPolicyException.class, ()->policies.add(new QuantityPolicy(store.getItems().keySet(), 0, -1)));
    }

    @Test
    void quantityPolicyMinBiggerThenMax() {
        assertThrows(QuantityPolicyException.class, ()->policies.add(new QuantityPolicy(store.getItems().keySet(), 6, 3)));
    }

    @Test
    void quantityPolicyForItemDoesntExist() throws ItemException, PolicyException {
        store.addItem("banana", 9.5, "cat2", "sub2", 7);
        item3 = store.searchItemById(2);
        policies.add(new QuantityPolicy(store.getItems().keySet(), 0, 12));
        store.setPurchasePolicy(new AndPolicy(policies));
        assertThrows(QuantityPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test
    void quantityPolicyBasketWrongMinQauntityItem() throws PolicyException, ItemException {
        user.getBasket(store).setQuantity(item1, 1);
        store.setPurchasePolicy(new QuantityPolicy(store.getItems().keySet(), 2, 4));
        assertThrows(PolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test
    void quantityPolicyBasketWrongMaxQuantityItem() throws PolicyException {
        user.getBasket(store).setQuantity(item1, 5);
        user.getBasket(store).setQuantity(item2, 4);
        store.setPurchasePolicy(new QuantityPolicy(store.getItems().keySet(), 2, 4));
        assertThrows(PolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test
    void timePolicyGoodDetails() throws Exception {
        user.getBasket(store).setQuantity(item1, 5);
        user.getBasket(store).setQuantity(item2, 4);
        store.setPurchasePolicy(new TimePolicy(store.getItems().keySet(), LocalTime.of(0,0)));
        user.purchaseCart(paymentSystem, deliverySystem);
    }

    @Test
    void timePolicyBadDetails() {
        user.getBasket(store).setQuantity(item1, 5);
        user.getBasket(store).setQuantity(item2, 4);
        store.setPurchasePolicy(new TimePolicy(store.getItems().keySet(), LocalTime.of(23,59)));
        assertThrows(PolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }
}
