package policies;

import user.Basket;

public class defaultDiscountPolicy extends simpleDiscountPolicy {
    @Override
    public double calculateDiscount(Basket purchaseBasket) {
        return 0;
    }

    @Override
    public void updateBasket(Basket purchaseBasket) {

    }
}
