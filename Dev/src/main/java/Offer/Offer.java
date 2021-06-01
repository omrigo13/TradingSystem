package Offer;

import store.Item;
import user.Subscriber;

import java.util.Collection;
import java.util.LinkedList;

public class Offer {

    private final Subscriber subscriber;
    private final Item item;
    private final int quantity;
    private double price;
    private boolean approved;
    private Collection<Subscriber> approvedOwners;
    private Collection<Subscriber> counteredOwners;

    public Offer(Subscriber subscriber, Item item, int quantity, double price) {
        this.subscriber = subscriber;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.approved = false;
        this.approvedOwners = new LinkedList<>();
        this.counteredOwners = new LinkedList<>();
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

    public void addApprovedOwner(Subscriber owner) { this.approvedOwners.add(owner); }

    public int getApprovedOwners() { return this.approvedOwners.size(); }

    public void addCounteredOwner(Subscriber owner) { this.counteredOwners.add(owner); }

    public int getCounteredOwners() { return this.counteredOwners.size(); }
}
