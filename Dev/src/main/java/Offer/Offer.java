package Offer;

import store.Item;
import user.Subscriber;

public class Offer {

    private final Subscriber subscriber;
    private final Item item;
    private final int quantity;
    private double price;
    private boolean approved;

    public Offer(Subscriber subscriber, Item item, int quantity, double price) {
        this.subscriber = subscriber;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.approved = false;
    }

    public Subscriber getSubscriber() { return subscriber; }

    public Item getItem() { return item; }

    public int getQuantity() { return quantity; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public String toString() {
        return ", user: " + subscriber.getUserName() + ", item: " + item.getName() + ", quantity: " + quantity + ", price: " + price;
    }

    public void approve() { this.approved = true; }

    public boolean isApproved() { return this.approved; }
}
