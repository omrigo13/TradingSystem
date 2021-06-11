package notifications;

import Offer.Offer;

import javax.persistence.Entity;

@Entity
public class CounterOfferNotification extends OfferNotification {

    public CounterOfferNotification(Offer offer) {
        super(offer);
    }

    public CounterOfferNotification() {

    }

    @Override
    public void notifyNotification() {

    }

    @Override
    public String print() {
        return "your offer of the item: " + this.getOffer().getItem() + " was countered offer with the price: " + this.getOffer().getPrice();
    }
}
