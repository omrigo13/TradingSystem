package notifications;

import Offer.Offer;

public class DeclinedOfferNotification extends OfferNotification {

    public DeclinedOfferNotification(Offer offer) {
        super(offer);
    }

    @Override
    public void notifyNotification() {

    }

    @Override
    public String print() {
        return "your offer of the item: " + this.getOffer().getItem() + " was declined and not approved";
    }
}
