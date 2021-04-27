package policies;

import exceptions.ItemException;
import exceptions.policyException;
import store.Item;
import user.Basket;

import java.util.Collection;

public abstract class simpleDiscountPolicy implements discountPolicy {

    protected int discount;
    protected Collection<Item> items;

    public simpleDiscountPolicy(int discount, Collection<Item> items) {
        this.discount = discount;
        this.items = items;
    }

    public int getDiscount() { return discount; }

    public Collection<Item> getItems() { return items; }
}
