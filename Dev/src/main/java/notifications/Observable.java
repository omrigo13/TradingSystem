package notifications;

import Offer.Offer;
import persistence.Repo;
import review.Review;
import store.Item;
import store.Store;
import user.Basket;
import user.Subscriber;
import user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
@Entity
public class Observable implements Serializable {
    @Id
    private int storeId;

    @ManyToMany
    private Collection<Subscriber> observers = new HashSet<>();

    public Observable(Store store) {
        this.storeId = store.getId();
    }

    public Observable() {

    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setObservers(Collection<Subscriber> observers) {
        this.observers = observers;
    }


    public void subscribe(Subscriber observer){
        if(!observers.contains(observer))
            observers.add(observer);
    }

    public void unsubscribe(Subscriber observer){
        if(observers.contains(observer))
            observers.remove(observer);
    }

    //notifies store owner upon purchase
    public void notifyPurchase(Store store, User buyer, Map<Item, Integer> basket){
        Map<Item, Integer> newMap = new HashMap<>();
        for (Item i:basket.keySet()) {
            newMap.put(i, basket.get(i));
        }

        for (Offer offer: buyer.getOffers(store))
            newMap.put(offer.getItem(), offer.getQuantity());

        PurchaseNotification notification = new PurchaseNotification(store, buyer, newMap);
        for (Subscriber s: observers) {
            s.notifyNotification(notification);
        }
    }

    //notifies store owner store status change (active/not active)
    public void notifyStoreStatus(String storeId, boolean isActive){
        StoreStatusNotification notification = new StoreStatusNotification(storeId, isActive);
        for (Subscriber s: observers) {
            s.notifyNotification(notification);
        }
    }

    //system manager can delete a subscriber from the system
    public void notifySubscriberRemove(){
        SubscriberRemoveNotification notification = new SubscriberRemoveNotification();
        //todo implement after requirement is fulfilled.
        for (Subscriber s: observers) {
            s.notifyNotification(notification);
        }
    }

    public void notifyItemReview(Subscriber subscriber, Review review) {
        ItemReviewNotification notification = new ItemReviewNotification(review);
        for (Subscriber s: observers) {
            if(s != subscriber)
                s.notifyNotification(notification);
        }
    }

    public void notifyLotteryStatus(){
        //todo: implement
        LotteryStatusNotification notification = new LotteryStatusNotification();
        for (Subscriber s: observers) {
            s.notifyNotification(notification);
        }
    }

    public void notifyMessage(User sender, String message){
        MessageNotification notification = new MessageNotification(sender, message);
        for (Subscriber s: observers) {
            s.notifyNotification(notification);
        }
    }

    public void notifyNewOffer(Offer offer) {
        OfferNotification notification = new OfferNotification(offer);
        for (Subscriber s: observers) {
            s.notifyNotification(notification);
        }
    }

    public void notifyApprovedOffer(Offer offer) {
        ApprovedOfferNotification notification = new ApprovedOfferNotification(offer);
        offer.getSubscriber().notifyNotification(notification);
    }

    public void notifyDeclinedOffer(Offer offer) {
        DeclinedOfferNotification notification = new DeclinedOfferNotification(offer);
        offer.getSubscriber().notifyNotification(notification);
    }

    public void notifyCounterOffer(Offer offer) {
        CounterOfferNotification notification = new CounterOfferNotification(offer);
        offer.getSubscriber().notifyNotification(notification);
    }

    public Collection<Subscriber> getObservers() {
        return observers;
    }

    public void notifyRoleRemove(Subscriber remover, Subscriber toRemove, int storeId) {
        OwnerOrManagerRemovedNotification n = new OwnerOrManagerRemovedNotification();
        n.setRemover(remover);
        n.setStoreId(storeId);
        toRemove.notifyNotification(n);

        unsubscribe(toRemove);
        Repo.merge(this);

    }

    public void notifyRoleAppointment(Subscriber assignor, Subscriber toAssign, int storeId, String role){
        AppointRoleNotification n = new AppointRoleNotification(assignor, role, storeId);
        subscribe(toAssign);
        Repo.merge(this);

        toAssign.notifyNotification(n);


    }
}
