package notifications;

import review.Review;

public class ItemReviewNotification extends Notification{

    private Review review;

    public ItemReviewNotification(Review review) {
        this.review = review;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
