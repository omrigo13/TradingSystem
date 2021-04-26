package policies;

import user.Basket;

import java.util.Collection;

public class maxDiscountPolicy extends compoundDiscountPolicy {

    public maxDiscountPolicy(Collection<simpleDiscountPolicy> discountPolicies) {
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
