package notifications;

import exceptions.ItemException;
import org.junit.jupiter.api.Test;
//import org.testng.annotations.Test;
import org.testng.*;
import review.Review;
import store.Item;
import store.Store;
import user.Basket;
import user.Subscriber;
import user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.testng.AssertJUnit.*;


public class NotificationsTests {

    //test: notifyPurchase, notifyStoreStatus, notifyItemReview
    //later test: notifySubscriberRemove, notifyLotteryStatus, notifyMessage

    private Store store1;
    private Item item1, item2;

    private Subscriber subscriber1;
    private User buyer1;
    private Basket basket1;
    private Subscriber buyer2;
    private Basket basket2;



    void createUsers(){
        subscriber1 = new Subscriber(10, "user1");
        buyer1 = new User();
        buyer2 = new Subscriber(11, "buyer2");
    }

    void openStoreAndAddItems() throws ItemException {
        createUsers();
        store1 = new Store();
        store1.subscribe(subscriber1);
        item1 = new Item(1, "item1", 10, "category1", null, -1);
        item2 = new Item(2, "item2", 10, "category2", null, -1);
        store1.addItem(item1.getName(), item1.getPrice(), item1.getCategory(), null, 20);
        store1.addItem(item2.getName(), item2.getPrice(), item2.getCategory(), null, 20);

    }

    void addItemsToBasket(){
//        basket1.addItem(item1, 5);
//        basket2.addItem(item2, 10);
//        basket1 = new Basket(store1, new ConcurrentHashMap<>());
//        basket2 = new Basket(store1, new ConcurrentHashMap<>());

        buyer1.getBasket(store1).addItem(item1, 5);
        buyer2.getBasket(store1).addItem(item2, 10);
    }

    @Test
    void subscribeOpenStore() throws ItemException {
        openStoreAndAddItems();
        assertTrue(store1.getObservable().getObservers().contains(subscriber1));
    }

    @Test
    void updateStoreStatusWhenOwnerLoggedOut() throws ItemException {
        openStoreAndAddItems();
        subscriber1.setLoggedIn(false);
        assertTrue(store1.isActive() == true);

        store1.setNotActive();
        Map<Notification, Boolean> notifications = subscriber1.getNotifications();
        System.out.println(notifications.toString());
        assertTrue(((StoreStatusNotification)(notifications.keySet().toArray()[0])).isActive() == false);
        assertTrue(((Boolean)notifications.values().toArray()[0] == false));
    }

    @Test
    void updateStoreStatusWhenOwnerLoggedIn() throws ItemException {
        openStoreAndAddItems();
        subscriber1.setLoggedIn(true);
        assertTrue(store1.isActive() == true);

        store1.setNotActive();
        Map<Notification, Boolean> notifications = subscriber1.getNotifications();
        System.out.println(notifications.toString());
        assertTrue(((StoreStatusNotification)(notifications.keySet().toArray()[0])).isActive() == false);
    }

    @Test
    void purchaseWhenOwnerLoggedOut() throws ItemException {
        openStoreAndAddItems();
        addItemsToBasket();
        subscriber1.setLoggedIn(false);

        System.out.println( buyer1.getBasket(store1).getItems().toString());
        store1.notifyPurchase(buyer1, buyer1.getBasket(store1).getItems());

        Map<Notification, Boolean> notifications = subscriber1.getNotifications();
        assertTrue(((PurchaseNotification)(notifications.keySet().toArray()[0])).getBasket().toString().contains("item1"));

        buyer1.getBasket(store1).getItems().clear(); //after this line, basket is empty

        assertTrue(((PurchaseNotification)(notifications.keySet().toArray()[0])).getBasket().toString().contains("item1")); //should receive the basket purchased, although it has been deleted


    }

    @Test
    void reviewItemWhenOwnerLoggedOut() throws ItemException {
        openStoreAndAddItems();
        addItemsToBasket();
        subscriber1.setLoggedIn(false);

        Review review = new Review(buyer1, store1, item1, "this is my review");

        store1.notifyItemOpinion(buyer2, review);

        Map<Notification, Boolean> notifications = subscriber1.getNotifications();
        System.out.println(notifications.toString());
        assertTrue(((ItemReviewNotification)(notifications.keySet().toArray()[0])).getReview().toString().contains("this is my review"));
        assertTrue(((Boolean)(notifications.values().toArray()[0])) == false);

    }


}


