package Offer;

import java.io.Serializable;
import java.util.Objects;

public class OfferId implements Serializable {

    private int id;
    private int store_id;

    public OfferId() {
    }

    public OfferId(int id, int store_id) {
        this.id = id;
        this.store_id = store_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfferId offerId = (OfferId) o;
        return id == offerId.id && store_id == offerId.store_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, store_id);
    }
}
