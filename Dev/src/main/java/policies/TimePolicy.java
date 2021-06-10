package policies;

import exceptions.PolicyException;
import exceptions.TimePolicyException;
import store.Item;
import user.Basket;

import javax.persistence.CollectionTable;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@Entity
public class TimePolicy extends SimplePurchasePolicy {

    @ManyToMany
    @CollectionTable(name = "time_policy_items")
    private Collection<Item> items;
    private LocalTime time;

    public TimePolicy(int id, Collection<Item> items, LocalTime time) {
        super(id);
        this.items = items;
        this.time = time;
    }

    public TimePolicy() {
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) throws PolicyException {
        LocalTime nowTime = LocalTime.now();
        String systemTime = nowTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        String policyTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
        for(Item item: items)
            if(!purchaseBasket.getItems().containsKey(item))
                throw new TimePolicyException();
        return systemTime.compareTo(policyTime) > 0;
    }
}
