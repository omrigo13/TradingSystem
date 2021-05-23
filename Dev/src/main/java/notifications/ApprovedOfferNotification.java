package notifications;

import Offer.Offer;

public class ApprovedOfferNotification extends OfferNotification {

    public ApprovedOfferNotification(Offer offer) {
        super(offer);
    }

    @Override
    public void notifyNotification() {

    }

    @Override
    public String print() {
        return "your offer of the item: " + this.getOffer().getItem() + " was approved";
    }
}
