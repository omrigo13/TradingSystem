package policies;

import exceptions.XorPolicyException;
import exceptions.PolicyException;
import jdk.jfr.Enabled;
import user.Basket;

import javax.persistence.Entity;
import java.util.Collection;
@Entity
public class XorPolicy extends CompoundPurchasePolicy {

    public XorPolicy(int id, Collection<PurchasePolicy> purchasePolicies) {
        super(id, purchasePolicies);
    }

    public XorPolicy() {
    }

    @Override
    // only two policies can be in xor and not more then that
    public boolean isValidPurchase(Basket purchaseBasket) throws PolicyException {
        boolean first, second;
        if(purchasePolicies.size() == 0)
            return true;
        if(purchasePolicies.size() == 1)
            return purchasePolicies.stream().toList().get(0).isValidPurchase(purchaseBasket);
        if(purchasePolicies.size() == 2) {
            try { first = purchasePolicies.stream().toList().get(0).isValidPurchase(purchaseBasket); }
            catch (PolicyException pe) { first = false; }

            try { second = purchasePolicies.stream().toList().get(1).isValidPurchase(purchaseBasket); }
            catch (PolicyException pe) { second = false; }

            boolean xorValue = ((first && !second) || (!first && second));
            if(!xorValue)
                throw new XorPolicyException();
        }
        return true;
    }
}
