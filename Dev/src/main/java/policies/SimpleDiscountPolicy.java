package policies;

import store.Item;

import java.util.Collection;
import java.util.LinkedList;

public abstract class SimpleDiscountPolicy implements DiscountPolicy {

    protected int discount;
    protected Collection<Item> items;

    public SimpleDiscountPolicy(int discount, Collection<Item> items) {
        this.discount = discount;
        this.items = items;
    }

    public int getDiscount() { return discount; }

    public Collection<Item> getItems() { return items; }

    public Collection<DiscountPolicy> getDiscountPolicies() {
        return new LinkedList<>();
    }
}
