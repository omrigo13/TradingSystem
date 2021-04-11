package review;

import store.Item;
import store.Store;
import user.User;

public class Review {

    private User user;
    private Store store;
    private Item item;
    private String review;

    public Review(User user, Store store, Item item, String review) {
        this.user = user;
        this.store = store;
        this.item = item;
        this.review = review;
    }

    public String getReview() {return review; }

    public void editReview(String review) {this.review = review; }
}
