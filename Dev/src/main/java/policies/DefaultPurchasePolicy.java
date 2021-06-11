package policies;

import persistence.Repo;
import user.Basket;

import javax.persistence.Entity;

@Entity
public class DefaultPurchasePolicy extends SimplePurchasePolicy {
    private static DefaultPurchasePolicy p = null;
    public DefaultPurchasePolicy(){
        super(-1);
    }

    public static DefaultPurchasePolicy getInstance(){
        if(p==null){
            p = new DefaultPurchasePolicy();
            Repo.merge(p);
        }
        return p;
    }

    @Override
    public boolean isValidPurchase(Basket purchaseBasket) { return true; }
}
