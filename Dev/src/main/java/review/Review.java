package review;

import store.Item;
import store.Store;
import user.User;

import javax.persistence.*;

@Entity
public class Review {

    @Id
    @GeneratedValue
    private int id;
    @Transient
    private User user;
    @ManyToOne
    private Store store;
    @ManyToOne
    private Item item;
    private String review;

    public Review() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Review(User user, Store store, Item item, String review) {
        this.user = user;
        this.store = store;
        this.item = item;
        this.review = review;
    }

    public String getReview() {return review; }

    public void editReview(String review) {this.review = review; }

    @Override
    public String toString() {
        return "Review{" +
                "user=" + user +
                ", store=" + store +
                ", item=" + item +
                ", review='" + review + '\'' +
                '}';
    }
}
