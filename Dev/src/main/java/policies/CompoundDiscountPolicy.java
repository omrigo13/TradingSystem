package policies;

import store.Item;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CompoundDiscountPolicy implements DiscountPolicy {

    protected Collection<DiscountPolicy> discountPolicies;
    protected int discount;
    protected Collection<Item> items;

    public CompoundDiscountPolicy(Collection<DiscountPolicy> discountPolicies) {

        if(discountPolicies == null)
            this.discountPolicies = new ArrayList<>();
        else
            this.discountPolicies = discountPolicies;
        this.items = new ArrayList<>();
    }

    public void add(SimpleDiscountPolicy discountPolicy) { this.discountPolicies.add(discountPolicy); }

    public void remove(SimpleDiscountPolicy discountPolicy) { this.discountPolicies.remove(discountPolicy); }

    public Collection<DiscountPolicy> getDiscountPolicies() { return this.discountPolicies; }

    public int getDiscount() { return discount; }

    public Collection<Item> getItems() { return items; }
}
