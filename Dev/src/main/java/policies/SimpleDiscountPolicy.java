package policies;

import store.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
//@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class SimpleDiscountPolicy implements DiscountPolicy {


    protected int discount;
    @OneToMany
    @MapsId
    protected Collection<Item> items = new ArrayList<>();

    public SimpleDiscountPolicy(int discount, Collection<Item> items) {
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
