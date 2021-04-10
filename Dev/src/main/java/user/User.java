package user;

import exceptions.NotLoggedInException;
import externalServices.DeliveryData;
import externalServices.DeliverySystem;
import externalServices.PaymentData;
import externalServices.PaymentSystem;
import store.Item;
import store.Store;

import java.util.HashMap;
import java.util.Map;

public class User {

    protected final Map<Store, Basket> baskets;

    public User() {
        this(new HashMap<>());
    }

    User(Map<Store, Basket> baskets) {
        this.baskets = baskets;
    }

    public Map<Store, Basket> getCart()
    {
        return baskets;
    }

    public void makeCart(User from)
    {
        if (baskets.isEmpty())
            baskets.putAll(from.getCart());
    }

    public Subscriber getSubscriber() throws NotLoggedInException {
        throw new NotLoggedInException();
    }

    public Basket getBasket(Store store) {

        Basket basket = baskets.get(store);
        if (basket == null) {
            basket = new Basket(store, this, new HashMap<>());
            baskets.put(store, basket);
        }
        return basket;
    }

    public void addCartToPurchases(Map<Store, String> details) {
    }

    public void resetCart() {baskets.clear(); }

    public void purchaseCart(PaymentSystem paymentSystem, DeliverySystem deliverySystem) throws Exception { // TODO exception

        double totalPrice = 0;
        Map<Store, String> storePurchaseDetails = new HashMap<>();
        totalPrice = processCartAndCalculatePrice(totalPrice, storePurchaseDetails);
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
    }

    private double processCartAndCalculatePrice(double totalPrice, Map<Store, String> storePurchaseDetails) throws Exception {
        StringBuilder purchaseDetails = new StringBuilder();
        for (Map.Entry<Store, Basket> storeBasketEntry : baskets.entrySet()) {
            Store store = storeBasketEntry.getKey();
            Map<Item, Integer> basket = storeBasketEntry.getValue().getItems();
            double price = store.processBasketAndCalculatePrice(basket, purchaseDetails);
            totalPrice += price;
            purchaseDetails.append("Total basket price: ").append(price).append("\n");
            storePurchaseDetails.put(store, purchaseDetails.toString());
        }
        return totalPrice;
    }
}
