package policies;

import store.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

@Entity
public abstract class CompoundDiscountPolicy extends DiscountPolicy {
    @ManyToMany
    @CollectionTable(name = "compound_policy_discount_policies")
    protected Collection<DiscountPolicy> discountPolicies;
    protected int discount;
    @ManyToMany
    @CollectionTable(name = "compound_policy_items")
    protected Collection<Item> items;
//    @Id
//    @GeneratedValue
//    private Integer id;

    public CompoundDiscountPolicy(int id, Collection<DiscountPolicy> discountPolicies) {
        super(id);
        if(discountPolicies == null)
            this.discountPolicies = new LinkedList<>();
        else
            this.discountPolicies = discountPolicies;
        this.items = new LinkedList<>();
    }

    public CompoundDiscountPolicy() {
    }

    public void add(SimpleDiscountPolicy discountPolicy) { this.discountPolicies.add(discountPolicy); }

    public void remove(SimpleDiscountPolicy discountPolicy) { this.discountPolicies.remove(discountPolicy); }

    public Collection<DiscountPolicy> getDiscountPolicies() { return this.discountPolicies; }

    public int getDiscount() { return discount; }

    public Collection<Item> getItems() { return items; }

//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getId() {
//        return id;
//    }
}
