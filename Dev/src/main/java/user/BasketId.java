package user;

import store.Store;

import java.io.Serializable;
import java.util.Objects;

public class BasketId implements Serializable {

    private Subscriber subscriber;

    private Store store;

    public BasketId(Subscriber subscriber, Store store) {
        this.subscriber = subscriber;
        this.store = store;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketId basketId = (BasketId) o;
        return Objects.equals(subscriber, basketId.subscriber) && Objects.equals(store, basketId.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriber, store);
    }
}
