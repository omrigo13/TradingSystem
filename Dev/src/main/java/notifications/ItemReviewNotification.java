package notifications;

import review.Review;

import javax.persistence.*;

@Entity
public class ItemReviewNotification extends Notification{

    @ManyToOne(cascade = CascadeType.ALL)
//    @Transient
    private Review review;

    public ItemReviewNotification(Review review) {
        this.review = review;
    }

    public ItemReviewNotification() {

    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Override
    public void notifyNotification() {

    }

    @Override
    public String toString() {
        return "ItemReviewNotification{" +
                "review=" + review +
                '}';
    }

    @Override
    public String print() {
        return "ItemReviewNotification{" +
                "review=" + review +
                '}';
    }
}
