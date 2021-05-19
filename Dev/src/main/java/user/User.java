package user;

import Offer.Offer;
import exceptions.*;
import externalServices.DeliveryData;
import externalServices.DeliverySystem;
import externalServices.PaymentData;
import externalServices.PaymentSystem;
import policies.DiscountPolicy;
import policies.PurchasePolicy;
import store.Item;
import store.Store;

import java.text.SimpleDateFormat;
import java.util.*;
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

    public Collection<Offer> getOffers(Store store) {
        // overridden in subclass
        return new LinkedList<Offer>();
    }

    public void purchaseCart(PaymentSystem paymentSystem, DeliverySystem deliverySystem, PaymentData paymentData, DeliveryData deliveryData) throws InvalidActionException {

        double totalPrice = 0;
        Map<Store, String> storePurchaseDetails = new HashMap<>();
        totalPrice = processCartAndCalculatePrice(totalPrice, storePurchaseDetails);
        if(totalPrice == 0)
            return;
        paymentData.setPaymentValue(totalPrice);
        try {
            paymentSystem.connect();
            paymentSystem.pay(paymentData);
            deliverySystem.connect();
            deliverySystem.deliver(deliveryData);
        }
        catch (PaymentSystemException pe) {
            paymentSystem.cancel(paymentData);
            // for each store, rollback the basket (return items to inventory)
            for (Map.Entry<Store, Basket> entry : baskets.entrySet())
                entry.getKey().rollBack(entry.getValue().getItems());
        }
        catch (DeliverySystemException de) {
            paymentSystem.cancel(paymentData);
            deliverySystem.cancel(deliveryData);
            for (Map.Entry<Store, Basket> entry : baskets.entrySet())
                entry.getKey().rollBack(entry.getValue().getItems());
        }

        // add each purchase details string to the store it was purchased from
        for (Map.Entry<Store, String> entry : storePurchaseDetails.entrySet())
            entry.getKey().addPurchase(entry.getValue());

        for (Map.Entry<Store, Basket> storeBasketEntry : baskets.entrySet()) {
            Store store = storeBasketEntry.getKey();
            Map<Item, Integer> basket = storeBasketEntry.getValue().getItems();
            store.notifyPurchase(this, basket);
        }

        addCartToPurchases(storePurchaseDetails);
        baskets.clear();
    }

    private double processCartAndCalculatePrice(double totalPrice, Map<Store, String> storePurchaseDetails) throws ItemException, PolicyException {
        boolean validPolicy;
        PurchasePolicy storePurchasePolicy;
        DiscountPolicy storeDiscountPolicy;
        for (Map.Entry<Store, Basket> storeBasketEntry : baskets.entrySet()) {

            storePurchasePolicy = storeBasketEntry.getKey().getPurchasePolicy();
            storeDiscountPolicy = storeBasketEntry.getKey().getDiscountPolicy();
            validPolicy = storePurchasePolicy.isValidPurchase(storeBasketEntry.getValue());
            if(!validPolicy)
                throw new PolicyException();

            StringBuilder purchaseDetails = new StringBuilder();
            Store store = storeBasketEntry.getKey();
            Basket basket = storeBasketEntry.getValue();
            Collection<Offer> userOffers = this.getOffers(store);

            double price = store.processBasketAndCalculatePrice(basket, purchaseDetails, storeDiscountPolicy, userOffers);

            for (Map.Entry<Integer, Offer> offer: store.getStoreOffers().entrySet()) {
                if(userOffers.contains(offer.getValue()) && offer.getValue().isApproved())
                    store.getStoreOffers().remove(offer.getKey());
            }

            totalPrice += price;
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            if(store.getTotalValuePerDay().containsKey(date))
                store.updateTotalValuePerDay(date, store.getTotalValuePerDay().get(date) + price);
            else
                store.addTotalValuePerDay(date, price);
            purchaseDetails.append("purchase date is: ").append(date).append("\n");
            purchaseDetails.append("Total basket price: ").append(price).append("\n");
            storePurchaseDetails.put(store, purchaseDetails.toString());
        }
        return totalPrice;
    }
}
