package policies;

import exceptions.policyException;
import store.Item;
import user.Basket;

import java.util.ArrayList;
import java.util.Collection;

public abstract class compoundDiscountPolicy implements discountPolicy {

    protected Collection<discountPolicy> discountPolicies;
    protected int discount;
    protected Collection<Item> items;

    public compoundDiscountPolicy(Collection<discountPolicy> discountPolicies) {

        if(discountPolicies == null)
            this.discountPolicies = new ArrayList<>();
        else
            this.discountPolicies = discountPolicies;
        this.items = new ArrayList<>();
    }

    public void add(simpleDiscountPolicy discountPolicy) { this.discountPolicies.add(discountPolicy); }

    public void remove(simpleDiscountPolicy discountPolicy) { this.discountPolicies.remove(discountPolicy); }

    public Collection<discountPolicy> getDiscountPolicies() { return this.discountPolicies; }

    public int getDiscount() { return discount; }

    public Collection<Item> getItems() { return items; }
}
