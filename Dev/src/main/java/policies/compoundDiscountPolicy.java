package policies;

import exceptions.policyException;
import user.Basket;

import java.util.ArrayList;
import java.util.Collection;

public abstract class compoundDiscountPolicy implements discountPolicy {

    protected Collection<simpleDiscountPolicy> discountPolicies;

    public compoundDiscountPolicy(Collection<simpleDiscountPolicy> discountPolicies) {

        if(discountPolicies == null)
            this.discountPolicies = new ArrayList<>();
        else
            this.discountPolicies = discountPolicies;
    }

    public abstract double calculateDiscount(Basket purchaseBasket) throws policyException;

    public abstract void updateBasket(Basket purchaseBasket);

    public void add(simpleDiscountPolicy discountPolicy) { this.discountPolicies.add(discountPolicy); }

    public void remove(simpleDiscountPolicy discountPolicy) { this.discountPolicies.remove(discountPolicy); }

    public Collection<simpleDiscountPolicy> getDiscountPolicies() { return this.discountPolicies; }
}
