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
import user.Basket;
import user.User;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class purchasePolicyTest {

    private User user;
    private Collection<simplePurchasePolicy> policies = new ArrayList<>();

    @Mock private PaymentSystem paymentSystem;
    @Mock private DeliverySystem deliverySystem;

    @Spy private Store store;
    @Spy private Item item1, item2, item3;

    @BeforeEach
    void setUp() throws ItemException {
        user = new User();
        user.makeCart(user);
        store.setPurchasePolicy(new defaultPurchasePolicy());
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

    @Test //should be here {1,0} {0,1}
    void xorPolicyByItemGoodDetails() throws ItemException, policyException {
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 12));
        policies.add(new quantityPolicy(store.getItems().keySet(), 6, 12));
        store.setPurchasePolicy(new xorPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);

        updateDetails();
        policies.clear();
        policies.add(new quantityPolicy(store.getItems().keySet(), 6, 12));
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 12));
        store.setPurchasePolicy(new xorPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);
    }

    @Test
    // //should be here {1,1} {0,0}
    void xorPolicyByItemBothPoliciesValidOrNotValid() throws ItemException, policyException {
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 12));
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 12));
        store.setPurchasePolicy(new xorPolicy(policies));
        assertThrows(xorPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));

        updateDetails();
        policies.clear();
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 3));
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 4));
        store.setPurchasePolicy(new xorPolicy(policies));
        assertThrows(xorPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test //should be here {1,1} {0,1} {1,0}
    void orPolicyByCategoryGoodDetails() throws ItemException, policyException {
        policies.add(new quantityPolicy(store.searchItems(null, null, "cat2"), 0, 6));
        policies.add(new quantityPolicy(store.searchItems(null, null, "cat2"), 6, 12));
        store.setPurchasePolicy(new orPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);

        updateDetails();
        policies.clear();
        policies.add(new quantityPolicy(store.searchItems(null, null, "cat2"), 6, 12));
        policies.add(new quantityPolicy(store.searchItems(null, null, "cat2"), 0, 6));
        store.setPurchasePolicy(new orPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);

        updateDetails();
        policies.clear();
        policies.add(new quantityPolicy(store.searchItems(null, null, "cat2"), 0, 8));
        policies.add(new quantityPolicy(store.searchItems(null, null, "cat2"), 4, 8));
        store.setPurchasePolicy(new orPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);
    }

    @Test //should be here {0,0}
    void orPolicyByCategoryBothPoliciesNotValid() throws ItemException, policyException {
        policies.add(new quantityPolicy(store.searchItems(null, null, "cat2"), 0, 3));
        policies.add(new quantityPolicy(store.searchItems(null, null, "cat2"), 0, 4));
        store.setPurchasePolicy(new orPolicy(policies));
        assertThrows(orPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test //should be here {1,1}
    void andPolicyByItemGoodDetails() throws ItemException, policyException {
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 12));
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 12));
        store.setPurchasePolicy(new andPolicy(policies));
        user.purchaseCart(paymentSystem, deliverySystem);
    }

    @Test //should be here {0,1} {1,0} {0,0}
    void andPolicyByItemAtLeastOnePolicyNotValid() throws ItemException, policyException {
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 12));
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 4));
        store.setPurchasePolicy(new andPolicy(policies));
        assertThrows(andPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));

        updateDetails();
        policies.clear();
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 3));
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 12));
        store.setPurchasePolicy(new andPolicy(policies));
        assertThrows(andPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));

        updateDetails();
        policies.clear();
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 3));
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 4));
        store.setPurchasePolicy(new andPolicy(policies));
        assertThrows(andPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test
    void quantityPolicyMinMaxQuantityBelowZero() throws ItemException, policyException {
        assertThrows(quantityPolicyException.class, ()->policies.add(new quantityPolicy(store.getItems().keySet(), -1, 0)));
        assertThrows(quantityPolicyException.class, ()->policies.add(new quantityPolicy(store.getItems().keySet(), 0, -1)));
    }

    @Test
    void quantityPolicyMinBiggerThenMax() {
        assertThrows(quantityPolicyException.class, ()->policies.add(new quantityPolicy(store.getItems().keySet(), 6, 3)));
    }

    @Test
    void quantityPolicyForItemDoesntExist() throws ItemException, policyException {
        store.addItem("banana", 9.5, "cat2", "sub2", 7);
        item3 = store.searchItemById(2);
        policies.add(new quantityPolicy(store.getItems().keySet(), 0, 12));
        store.setPurchasePolicy(new andPolicy(policies));
        assertThrows(quantityPolicyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test
    void quantityPolicyBasketWrongMinQauntityItem() throws policyException, ItemException {
        user.getBasket(store).setQuantity(item1, 1);
        store.setPurchasePolicy(new quantityPolicy(store.getItems().keySet(), 2, 4));
        assertThrows(policyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }

    @Test
    void quantityPolicyBasketWrongMaxQuantityItem() throws policyException {
        user.getBasket(store).setQuantity(item1, 5);
        user.getBasket(store).setQuantity(item2, 4);
        store.setPurchasePolicy(new quantityPolicy(store.getItems().keySet(), 2, 4));
        assertThrows(policyException.class, ()->user.purchaseCart(paymentSystem, deliverySystem));
    }
}
