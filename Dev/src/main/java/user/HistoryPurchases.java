package user;

import store.Item;
import store.Store;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
@Entity
@IdClass(HistoryPurchaseId.class)
public class HistoryPurchases {

    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<Item> items;
    @ManyToOne
    @Id
    private Store store;

    @Id
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public HistoryPurchases(Store store, String userName) {
        this.store = store;
        this.items = new HashSet<>();
        this.userName = userName;
    }

    public HistoryPurchases() {

    }

    public Collection<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        this.items.add(item);
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }
}
