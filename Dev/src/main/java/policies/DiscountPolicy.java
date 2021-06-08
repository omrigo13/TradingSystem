package policies;

import exceptions.PolicyException;
import store.Item;
import user.Basket;

import javax.persistence.*;
import java.util.Collection;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class DiscountPolicy {

    @Id
    private int discount_id;

    public DiscountPolicy(int discount_id) {
        this.discount_id = discount_id;
    }

    public DiscountPolicy() {

    }

    public int getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(int id) {
        this.discount_id = id;
    }

    public abstract double cartTotalValue(Basket purchaseBasket) throws PolicyException;

    public abstract int getDiscount();

    public abstract Collection<Item> getItems();

    public abstract Collection<DiscountPolicy> getDiscountPolicies();
}
