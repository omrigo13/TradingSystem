package notifications;

import review.Review;
import store.Item;
import user.Subscriber;
import user.User;

import java.util.*;

public class Observable {
    private Collection<Subscriber> observers = new HashSet<>();

    public void subscribe(Subscriber observer){
        if(!observers.contains(observer))
            observers.add(observer);
    }

    public void unsubscribe(Subscriber observer){
        if(observers.contains(observer))
            observers.remove(observer);
    }

    //notifies store owner upon purchase
    public void notifyPurchase(User buyer, Map<Item, Integer> basket){
        PurchaseNotification notification = new PurchaseNotification(buyer, basket);
        for (Subscriber s: observers) {
            s.notifyNotification(notification);
        }
    }

    //notifies store owner store status change (active/not active)
    public void notifyStoreStatus(boolean isActive){
        StoreStatusNotification notification = new StoreStatusNotification(isActive);
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

    public void notifyItemReview(Review review) {
        ItemReviewNotification notification = new ItemReviewNotification(review);
        for (Subscriber s: observers) {
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

    public Collection<Subscriber> getObservers() {
        return observers;
    }
}
