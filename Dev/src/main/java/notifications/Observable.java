package notifications;

import review.Review;
import store.Item;
import store.Store;
import user.Basket;
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
    public void notifyPurchase(Store store, User buyer, Map<Item, Integer> basket){
        Map<Item, Integer> newMap = new HashMap<>();
        for (Item i:basket.keySet()) {
            newMap.put(i, basket.get(i));
        }

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

    public void notifyRoleRemove(Subscriber remover, Subscriber toRemove, int storeId) {
        OwnerOrManagerRemovedNotification n = new OwnerOrManagerRemovedNotification();
        n.setRemover(remover);
        n.setStoreId(storeId);
        toRemove.notifyNotification(n);

        unsubscribe(toRemove);
    }

    public void notifyRoleAppointment(Subscriber assignee, Subscriber toAssign, int storeId, String role){
        AppointRoleNotification n = new AppointRoleNotification(assignee, role, storeId);
        subscribe(toAssign);
        toAssign.notifyNotification(n);

    }
}
