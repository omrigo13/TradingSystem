package notifications;

import Offer.Offer;

import javax.persistence.*;

@Entity

public class OfferNotification extends Notification {
    @OneToOne
    private Offer offer;

    public OfferNotification(Offer offer) {
        this.offer = offer;
    }

    public OfferNotification() {
    }

    @Override
    public void notifyNotification() {

    }

    @Override
    public String print() {
        return "new offer by: " + offer.getSubscriber().getUserName() + ", of the item: " + offer.getItem().getName() +
        ", quantity: " + offer.getQuantity() + ", price: " + offer.getPrice();
    }

    public Offer getOffer() { return this.offer; }
}
