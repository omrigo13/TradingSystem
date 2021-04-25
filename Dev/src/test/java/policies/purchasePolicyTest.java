package policies;

import org.junit.jupiter.api.Test;

public class purchasePolicyTest {

    //TODO should check these things
    //TODO check all compound policies xor, or , and with simple policiy from quantity of item, category while making purchaseCart

    @Test //should be here {1,0} {0,1}
    void xorPolicyByItemGoodDetails() {

    }

    @Test
    // //should be here {1,1} {0,0}
    void xorPolicyByItemBothPoliciesValidOrNotValid() {

    }

    @Test //should be here {1,1} {0,1} {1,0}
    void orPolicyByCategoryGoodDetails() {

    }

    @Test //should be here {0,0}
    void orPolicyByCategoryBothPoliciesNotValid() {

    }

    @Test //should be here {1,1}
    void andPolicyByItemGoodDetails() {

    }

    @Test //should be here {0,1} {1,0} {0,0}
    void andPolicyByItemAtLeastOnePolicyNotValid() {

    }

    @Test
    void quantityPolicyMinMaxQuantityBelowZero() {

    }

    @Test
    void quantityPolicyMinBiggerThenMax() {

    }

    @Test
    void quantityPolicyForItemDoesntExist() {

    }

    @Test
    void quantityPolicyBasketWrongMinQauntityItem() {

    }

    @Test
    void quantityPolicyBasketWrongMaxQuantityItem() {

    }
}
