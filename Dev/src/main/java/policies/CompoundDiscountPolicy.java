package policies;

import store.Item;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
public abstract class CompoundDiscountPolicy implements DiscountPolicy {

    protected Collection<DiscountPolicy> discountPolicies;
    protected int discount;
    protected Collection<Item> items;
    @Id
    @GeneratedValue
    private Integer id;

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

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
