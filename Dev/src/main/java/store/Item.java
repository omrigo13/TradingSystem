package store;

import exceptions.ItemException;
import exceptions.WrongPriceException;
import exceptions.WrongRatingException;
import review.Review;

import javax.persistence.*;
import java.util.Collection;
import java.util.LinkedList;
@Entity
@IdClass(ItemId.class)
public class Item {
    @Id
    private int item_id;
    @Id
    private int store_id;
    private String name;
    private double price;
    private String category;
    private String subCategory;
    private double rating;
    private boolean isLocked = false;
    @OneToMany
    private final Collection<Review> reviews = new LinkedList<>();
    private int amount;

    public Item() {}

    public Item(int store_id, int item_id, String name, double price, String category, String subCategory, double rating, int amount) {
        this.store_id = store_id;
        this.item_id = item_id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.subCategory = subCategory;
        this.rating = rating;
        this.amount = amount;
    }

    public int getItem_id() {
        return item_id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws ItemException {
        if(price < 0)
            throw new WrongPriceException("item price must be positive");
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String newSubCategory){ this.subCategory=newSubCategory;}

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) throws ItemException {
        if(rating < 0)
            throw new WrongRatingException("rating must be positive");
        this.rating = rating;
    }

    public String toString() { return "id:" + item_id +
            "\nname:" + name +
            "\nprice:" + price +
            "\ncategory:" + category +
            "\nsub category:" + subCategory +
            "\nrating:" + rating +
            "\namount:" + amount + '\n';}

    public void lock() { isLocked = true; }

    public void unlock() {isLocked = false; }

    public boolean isLocked() {return isLocked; }

    public void addReview(Review review) {reviews.add(review); }

    public Collection<Review> getReviews() {return reviews; }

    public void setAmount(int amount) { this.amount = amount; }

    public int getAmount() { return amount; }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}




