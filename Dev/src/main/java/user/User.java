package user;

import exceptions.ItemException;
import exceptions.NotLoggedInException;
import exceptions.policyException;
import externalServices.DeliveryData;
import externalServices.DeliverySystem;
import externalServices.PaymentData;
import externalServices.PaymentSystem;
import policies.defaultDiscountPolicy;
import policies.discountPolicy;
import policies.purchasePolicy;
import store.Item;
import store.Store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class User {

    protected final ConcurrentHashMap<Store, Basket> baskets;

    public User() {
        this(new ConcurrentHashMap<>());
    }

    User(ConcurrentHashMap<Store, Basket> baskets) {
        this.baskets = baskets;
    }

    public Map<Store, Basket> getCart()
    {
        return baskets;
    }

    public void makeCart(User from) {

        if (baskets.isEmpty())
            baskets.putAll(from.getCart());
    }

    public Subscriber getSubscriber() throws NotLoggedInException {

        throw new NotLoggedInException();
    }

    public Basket getBasket(Store store) {

        return baskets.computeIfAbsent(store, k -> new Basket(k, new ConcurrentHashMap<>()));
    }

    public void addCartToPurchases(Map<Store, String> details) {
        // overridden in subclass
    }

    public void purchaseCart(PaymentSystem paymentSystem, DeliverySystem deliverySystem) throws ItemException, policyException {

        double totalPrice = 0;
        boolean validPolicy;
        Map<Store, String> storePurchaseDetails = new HashMap<>();
        purchasePolicy storePurchasePolicy;
        discountPolicy storeDiscountPolicy = new defaultDiscountPolicy();
        for (Map.Entry<Store, Basket> storeBasketEntry : baskets.entrySet()) {
            storePurchasePolicy = storeBasketEntry.getKey().getPurchasePolicy();
            storeDiscountPolicy = storeBasketEntry.getKey().getDiscountPolicy();
            validPolicy = storePurchasePolicy.isValidPurchase(storeBasketEntry.getValue());
            if(!validPolicy)
                throw new policyException();
        }
        totalPrice = processCartAndCalculatePrice(totalPrice, storePurchaseDetails, storeDiscountPolicy);
        PaymentData paymentData = null;
        boolean paymentDone = false;
        try {
            paymentData = new PaymentData(totalPrice);
            paymentSystem.pay(paymentData);
            paymentDone = true;
            deliverySystem.deliver(new DeliveryData());
        } catch (Exception e) {
            if (paymentDone)
                paymentSystem.payBack(paymentData);
            // for each store, rollback the basket (return items to inventory)
            for (Map.Entry<Store, Basket> entry : baskets.entrySet())
                entry.getKey().rollBack(entry.getValue().getItems());
            throw e;
        }

        // add each purchase details string to the store it was purchased from
        for (Map.Entry<Store, String> entry : storePurchaseDetails.entrySet())
            entry.getKey().addPurchase(entry.getValue());

        addCartToPurchases(storePurchaseDetails);
        baskets.clear();
    }

    private double processCartAndCalculatePrice(double totalPrice, Map<Store, String> storePurchaseDetails, discountPolicy storeDiscountPolicy) throws ItemException {
        for (Map.Entry<Store, Basket> storeBasketEntry : baskets.entrySet()) {
            StringBuilder purchaseDetails = new StringBuilder();
            Store store = storeBasketEntry.getKey();
            Basket basket = storeBasketEntry.getValue();
            double price = store.processBasketAndCalculatePrice(basket, purchaseDetails, storeDiscountPolicy);
            totalPrice += price;
            purchaseDetails.append("Total basket price: ").append(price).append("\n");
            storePurchaseDetails.put(store, purchaseDetails.toString());
        }
        return totalPrice;
    }
}
