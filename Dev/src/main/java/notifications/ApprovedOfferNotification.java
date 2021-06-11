package notifications;

import Offer.Offer;

import javax.persistence.Entity;
import javax.persistence.Inheritance;

@Entity
public class ApprovedOfferNotification extends OfferNotification {

    public ApprovedOfferNotification(Offer offer) {
        super(offer);
    }

    public ApprovedOfferNotification() {

    }


    @Override
    public void notifyNotification() {

    }

    @Override
    public String print() {
        return "your offer of the item: " + this.getOffer().getItem() + " was approved";
    }
}
