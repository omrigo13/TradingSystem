package policies;

import store.Item;
import store.Store;

import java.util.Collection;
import java.util.LinkedList;
import javax.persistence.*;

public abstract class SimpleDiscountPolicy implements DiscountPolicy {

    protected int discount;
    protected Collection<Item> items;

    private Store store;

    public SimpleDiscountPolicy(int discount, Collection<Item> items) {
        this.discount = discount;
        this.items = items;
    }

    public SimpleDiscountPolicy() {

    }

    public int getDiscount() { return discount; }

    public Collection<Item> getItems() { return items; }

    public Collection<DiscountPolicy> getDiscountPolicies() {
        return new LinkedList<>();
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }
}
