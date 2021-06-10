package policies;

import store.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
@Entity
public abstract class SimpleDiscountPolicy extends DiscountPolicy {

    protected int discount;

    @ManyToMany
    @CollectionTable(name = "simple_discount_policy_items")
    protected Collection<Item> items = new LinkedList<>();

    public SimpleDiscountPolicy(int id, int discount, Collection<Item> items) {
        super(id);
        this.discount = discount;
        this.items = items;

    }

    public SimpleDiscountPolicy() {
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }

    public int getDiscount() { return discount; }

    public Collection<Item> getItems() { return items; }

    public Collection<DiscountPolicy> getDiscountPolicies() {
        return new LinkedList<>();
    }
}
