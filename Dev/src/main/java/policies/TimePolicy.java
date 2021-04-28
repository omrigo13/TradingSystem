package policies;

import exceptions.PolicyException;
import exceptions.TimePolicyException;
import store.Item;
import user.Basket;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class TimePolicy extends SimplePurchasePolicy {

    private final Collection<Item> items;
    private final LocalTime time;

    public TimePolicy(Collection<Item> items, LocalTime time) {
        this.items = items;
        this.time = time;
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
