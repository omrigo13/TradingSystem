package notifications;

import Offer.Offer;

import javax.persistence.Entity;

@Entity
public class DeclinedOfferNotification extends OfferNotification {

    public DeclinedOfferNotification(Offer offer) {
        super(offer);
    }

    public DeclinedOfferNotification() {

    }

    @Override
    public void notifyNotification() {

    }

    @Override
    public String print() {
        return "your offer of the item: " + this.getOffer().getItem() + " was declined and not approved";
    }
}
