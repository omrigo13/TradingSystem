package notifications;

import Offer.Offer;

public class CounterOfferNotification extends OfferNotification {

    public CounterOfferNotification(Offer offer) {
        super(offer);
    }

    @Override
    public void notifyNotification() {

    }

    @Override
    public String print() {
        return "your offer of the item: " + this.getOffer().getItem() + " was countered offer with the price: " + this.getOffer().getPrice();
    }
}
