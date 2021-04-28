package notifications;

import review.Review;
import store.Item;
import user.Subscriber;
import user.User;

import java.util.*;

public class Observable {
    private Collection<Subscriber> observers = new HashSet<>();

    public void subscribe(Subscriber observer){

    }

    public void unsubscribe(Subscriber observer){

    }

    //notifies store owner upon purchase
    public void notifyPurchase(User buyer, Map<Item, Integer> basket){
        for (Subscriber s: observers) {
            s.notifyObserverPurchase(buyer, basket);
        }
    }

    //notifies store owner store status change (active/not active)
    public void notifyStoreStatus(boolean isActive){
        for (Subscriber s: observers) {
            s.notifyObserverStoreStatus(isActive);
        }
    }

    //system manager can delete a subscriber from the system
    public void notifySubscriberRemove(){
        //todo implement after requirement is fulfilled.
    }

    public void notifyItemOpinion(Review review) {
        for (Subscriber s: observers) {
            s.notifyObserverItemOpinion(review);
        }
    }
}
