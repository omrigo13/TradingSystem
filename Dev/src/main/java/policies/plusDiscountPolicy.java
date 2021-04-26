package policies;

import user.Basket;

import java.util.Collection;

public class plusDiscountPolicy extends compoundDiscountPolicy {
    public plusDiscountPolicy(Collection<simpleDiscountPolicy> discountPolicies) {
        super(discountPolicies);
    }

    @Override
    public double calculateDiscount(Basket purchaseBasket) {
        return 0;
    }

    @Override
    public void updateBasket(Basket purchaseBasket) {

    }
}
